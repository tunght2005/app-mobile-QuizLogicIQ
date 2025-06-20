const nodemailer = require('nodemailer');

// Tạo transporter với cấu hình Gmail
const transporter = nodemailer.createTransport({
  host: 'smtp.gmail.com',
  port: 587,
  secure: false, // true for 465, false for other ports
  auth: {
    user: process.env.EMAIL_USER,
    pass: process.env.EMAIL_APP_PASSWORD // Sử dụng App Password thay vì mật khẩu thông thường
  }
});

// Kiểm tra kết nối email
transporter.verify(function (error, success) {
  if (error) {
    console.error('Lỗi cấu hình email:', error);
  } else {
    console.log('Máy chủ email sẵn sàng gửi tin nhắn');
  }
});

const sendVerificationEmail = async (email, verificationLink) => {
  try {
    const mailOptions = {
      from: `"Quiz App" <${process.env.EMAIL_USER}>`,
      to: email,
      subject: 'Xác thực Email',
      html: `
        <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
          <h2 style="color: #333;">Chào mừng đến với Quiz App!</h2>
          <p>Cảm ơn bạn đã đăng ký. Vui lòng xác thực email của bạn bằng cách nhấp vào nút bên dưới:</p>
          <div style="text-align: center; margin: 30px 0;">
            <a href="${verificationLink}" 
               style="background-color: #4CAF50; color: white; padding: 12px 24px; text-decoration: none; border-radius: 4px;">
              Xác thực Email
            </a>
          </div>
          <p>Nếu nút không hoạt động, bạn có thể nhấp vào đường dẫn sau:</p>
          <p><a href="${verificationLink}">${verificationLink}</a></p>
          <p>Liên kết này sẽ hết hạn trong 24 giờ.</p>
          <p>Nếu bạn không tạo tài khoản, vui lòng bỏ qua email này.</p>
        </div>
      `
    };

    const info = await transporter.sendMail(mailOptions);
    console.log('Đã gửi email xác thực:', info.messageId);
    return info;
  } catch (error) {
    console.error('Lỗi khi gửi email xác thực:', error);
    throw error;
  }
};

const sendVerificationSuccessEmail = async (email, name) => {
  try {
    const mailOptions = {
      from: `"Quiz App" <${process.env.EMAIL_USER}>`,
      to: email,
      subject: 'Xác thực Email Thành Công',
      html: `
        <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
          <h2 style="color: #333;">Xác thực Email Thành Công!</h2>
          <p>Chào ${name},</p>
          <p>Email của bạn đã được xác thực thành công. Bạn có thể:</p>
          <ul>
            <li>Đăng nhập vào tài khoản</li>
            <li>Truy cập tất cả tính năng của Quiz App</li>
            <li>Bắt đầu tạo và tham gia các bài quiz</li>
          </ul>
          <div style="text-align: center; margin: 30px 0;">
            <a href="${process.env.FRONTEND_URL}/login" 
               style="background-color: #4CAF50; color: white; padding: 12px 24px; text-decoration: none; border-radius: 4px;">
              Đăng nhập ngay
            </a>
          </div>
          <p>Cảm ơn bạn đã sử dụng Quiz App!</p>
        </div>
      `
    };

    const info = await transporter.sendMail(mailOptions);
    console.log('Đã gửi email xác thực thành công:', info.messageId);
    return info;
  } catch (error) {
    console.error('Lỗi khi gửi email xác thực thành công:', error);
    throw error;
  }
};

const sendPasswordResetEmail = async (email, resetLink) => {
  try {
    const mailOptions = {
      from: `"Quiz App" <${process.env.EMAIL_USER}>`,
      to: email,
      subject: 'Đặt lại Mật khẩu',
      html: `
        <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
          <h2 style="color: #333;">Đặt lại Mật khẩu</h2>
          <p>Bạn đã yêu cầu đặt lại mật khẩu. Nhấp vào nút dưới đây để tạo mật khẩu mới:</p>
          <div style="text-align: center; margin: 30px 0;">
            <a href="${resetLink}" 
               style="background-color: #4CAF50; color: white; padding: 12px 24px; text-decoration: none; border-radius: 4px;">
              Đặt lại Mật khẩu
            </a>
          </div>
          <p>Nếu nút không hoạt động, bạn có thể sao chép đường dẫn sau:</p>
          <p><a href="${resetLink}">${resetLink}</a></p>
          <p>Liên kết này sẽ hết hạn trong 1 giờ.</p>
          <p>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.</p>
        </div>
      `
    };

    const info = await transporter.sendMail(mailOptions);
    console.log('Đã gửi email đặt lại mật khẩu:', info.messageId);
    return info;
  } catch (error) {
    console.error('Lỗi khi gửi email đặt lại mật khẩu:', error);
    throw error;
  }
};

const sendNewPasswordEmail = async (email, newPassword, name) => {
  try {
    const mailOptions = {
      from: `"Quiz App" <${process.env.EMAIL_USER}>`,
      to: email,
      subject: 'Mật khẩu Mới của Bạn',
      html: `
        <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
          <h2 style="color: #333;">Mật khẩu Mới của Bạn</h2>
          <p>Chào ${name},</p>
          <p>Mật khẩu của bạn đã được đặt lại thành công. Dưới đây là mật khẩu mới:</p>
          <div style="background-color: #f5f5f5; padding: 15px; border-radius: 4px; margin: 20px 0; text-align: center;">
            <p style="font-size: 18px; font-weight: bold; margin: 0;">${newPassword}</p>
          </div>
          <p>Vui lòng đăng nhập với mật khẩu này và thay đổi ngay lập tức để đảm bảo an toàn.</p>
          <div style="text-align: center; margin: 30px 0;">
            <a href="${process.env.FRONTEND_URL}/login" 
               style="background-color: #4CAF50; color: white; padding: 12px 24px; text-decoration: none; border-radius: 4px;">
              Đăng nhập ngay
            </a>
          </div>
          <p style="color: #666; font-size: 14px;">Vì lý do bảo mật, vui lòng thay đổi mật khẩu sau khi đăng nhập.</p>
        </div>
      `
    };

    const info = await transporter.sendMail(mailOptions);
    console.log('Đã gửi email mật khẩu mới:', info.messageId);
    return info;
  } catch (error) {
    console.error('Lỗi khi gửi email mật khẩu mới:', error);
    throw error;
  }
};

module.exports = {
  sendVerificationEmail,
  sendVerificationSuccessEmail,
  sendPasswordResetEmail,
  sendNewPasswordEmail
};
