const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const crypto = require('crypto');
const admin = require('../config/firebase.config');
const User = require('../models/user.model');
const { sendVerificationEmail, sendNewPasswordEmail } = require('../services/email.service');

// Login with Google: xác thực idToken, chỉ lưu 1 user theo email, không gửi mail xác thực
exports.loginWithGoogle = async (req, res) => {
  try {
    const { idToken } = req.body;
    if (!idToken) return res.status(400).json({ message: 'Thiếu idToken' });

    // Xác thực token với Firebase
    const decoded = await admin.auth().verifyIdToken(idToken);
    const { email, name, uid: firebaseUid } = decoded;
    if (!email || !firebaseUid) return res.status(400).json({ message: 'Thiếu thông tin người dùng' });

    // Tìm user theo email (ưu tiên gom user)
    let user = await User.findOne({ email });

    if (user) {
      // Nếu user chưa có firebaseUid thì cập nhật
      if (!user.firebaseUid) {
        user.firebaseUid = firebaseUid;
        if (!user.loginMethods.includes('google')) user.loginMethods.push('google');
      }
      // Nếu user chưa xác thực (do đăng ký email), cập nhật isVerified
      if (!user.isVerified) user.isVerified = true;
      await user.save();
    } else {
      // Nếu chưa có user, tạo mới với isVerified: true
      user = new User({
        email,
        name,
        firebaseUid,
        isVerified: true,
        loginMethods: ['google']
      });
      await user.save();
    }

    // Đăng nhập thành công
    const token = jwt.sign({ userId: user._id }, process.env.JWT_SECRET, { expiresIn: '24h' });
    return res.json({
      message: 'Đăng nhập thành công',
      token,
      user: { email: user.email, name: user.name, isVerified: user.isVerified }
    });
  } catch (error) {
    console.error('Lỗi loginWithGoogle:', error);
    return res.status(500).json({ message: 'Lỗi máy chủ', error: error.message });
  }
};

// Register with Email (send verification email)
exports.registerWithEmail = async (req, res) => {
  try {
    const { email, password, name } = req.body;

    const existingUser = await User.findOne({ email });
    if (existingUser) {
      return res.status(400).json({ message: 'Người dùng đã tồn tại!' });
    }

    // Tạo mã xác thực ngẫu nhiên
    const verificationToken = crypto.randomBytes(32).toString('hex');
    const hashedPassword = await bcrypt.hash(password, 10);

    // Lưu user với isVerified = false
    const user = new User({
      email,
      name,
      password: hashedPassword,
      isVerified: false,
      verificationToken,
      loginMethods: ['email']
    });
    await user.save();

    // Gửi email xác thực
    const verificationLink = `${process.env.BACKEND_URL}/api/auth/verify-email?token=${verificationToken}`;
    await sendVerificationEmail(email, verificationLink);

    return res.status(201).json({
      message: 'Đăng ký thành công. Vui lòng kiểm tra email để xác thực.'
    });

  } catch (error) {
    console.error('Lỗi đăng ký:', error);
    return res.status(500).json({ message: 'Lỗi máy chủ', error: error.message });
  }
};


// Verify Email (activate user)
exports.verifyEmail = async (req, res) => {
  try {
    const { token } = req.query;
    console.log(' Nhận token xác thực:', token);

    const user = await User.findOne({ verificationToken: token });

    if (!user) {
      console.log('Không tìm thấy user với token:', token);
      return res.redirect(`${process.env.FRONTEND_URL}/email-verified?status=error&message=Token không hợp lệ.`);
    }

    user.isVerified = true;
    user.verificationToken = undefined;

    await user.save()
      .then(() => console.log('💾 Cập nhật MongoDB thành công: isVerified = true'))
      .catch(err => console.error(' Lỗi khi lưu user:', err));

    return res.redirect(`${process.env.FRONTEND_URL}/email-verified?status=success`);
  } catch (err) {
    console.error(' Lỗi hệ thống:', err.message);
    return res.redirect(`${process.env.BACKEND_URL}/email-verified?status=error&message=${encodeURIComponent(err.message)}`);
  }
};



// Login with Email (only if verified)
exports.loginWithEmail = async (req, res) => {
  try {
    const { email, password } = req.body;
    const user = await User.findOne({ email });
    if (!user) return res.status(404).json({ message: 'Không tìm thấy người dùng' });

    if (!await bcrypt.compare(password, user.password)) {
      return res.status(401).json({ message: 'Mật khẩu không hợp lệ' });
    }

    if (!user.isVerified) {
      return res.status(403).json({ message: 'Vui lòng xác thực email trước khi đăng nhập.' });
    }

    const token = jwt.sign({ userId: user._id }, process.env.JWT_SECRET, { expiresIn: '24h' });
    return res.json({ token, user: { email: user.email, name: user.name, isVerified: user.isVerified } });
  } catch (error) {
    console.error('Lỗi đăng nhập email:', error);
    return res.status(500).json({ message: 'Lỗi máy chủ', error: error.message });
  }
};

// Reset Password
exports.resetPassword = async (req, res) => {
  try {
    const { email } = req.body;
    const user = await User.findOne({ email });
    if (!user) {
      return res.status(404).json({ message: 'Không tìm thấy người dùng' });
    }

    const newPassword = crypto.randomBytes(8).toString('hex');
    const hashed = await bcrypt.hash(newPassword, 10);

    if (user.firebaseUid) {
      await admin.auth().updateUser(user.firebaseUid, { password: newPassword });
    }

    user.password = hashed;
    await user.save();
    await sendNewPasswordEmail(user.email, newPassword, user.name);

    return res.json({ message: 'Mật khẩu mới đã được gửi tới email của bạn.' });
  } catch (error) {
    console.error('Lỗi reset password:', error);
    return res.status(500).json({ message: 'Lỗi máy chủ', error: error.message });
  }
};

exports.verifyGoogleLogin = async (req, res) => {
  try {
    const { token } = req.query;
    const user = await User.findOne({ verificationToken: token });
    if (!user) {
      return res.redirect(`${process.env.FRONTEND_URL}/login?status=error&message=Token xác thực không hợp lệ hoặc đã hết hạn`);
    }

    // Cập nhật trạng thái xác thực
    user.isVerified = true;
    user.verificationToken = undefined;
    await user.save();

    // Tạo JWT token
    const authToken = jwt.sign({ userId: user._id }, process.env.JWT_SECRET, {
      expiresIn: '24h'
    });

    // Redirect về frontend với token
    return res.redirect(`${process.env.FRONTEND_URL}/login?status=success&token=${authToken}`);
  } catch (error) {
    res.redirect(`${process.env.FRONTEND_URL}/login?status=error&message=${encodeURIComponent(error.message)}`);
  }
};
