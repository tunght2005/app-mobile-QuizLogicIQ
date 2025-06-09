// src/server.js
import 'dotenv/config';  // chỉ 1 lần là đủ
import express from 'express';
import session from 'express-session';
import mongoose from 'mongoose';

import connectDB from './config/mongodb.js';
import passport from './middlewares/passport.js';
import authRoutes from './routes/authRoutes.js';
import { loginWithGoogleMobile } from './controllers/googleMobileController.js';

const app = express();
const PORT = process.env.PORT || 3000;

// 1. Kết nối database
connectDB();

// 2. Middleware toàn cục
app.use(express.json());
app.use(session({
  secret: process.env.SESSION_SECRET || 'cats',
  resave: false,
  saveUninitialized: false,
}));
// Khởi tạo Passport + session support
app.use(passport.initialize());
app.use(passport.session());

// 3. Web-flow Google OAuth (Passport)
app.get('/', (req, res) => {
  res.send('<a href="/auth/google">Đăng nhập bằng Google (Web)</a>');
});

// Đăng nhập với Google (web)
app.get('/auth/google',
  passport.authenticate('google', { scope: ['email','profile'] })
);

// Callback sau khi user cho phép
app.get('/google/callback',
  passport.authenticate('google', {
    successRedirect: '/protected',
    failureRedirect: '/auth/failure'
  })
);

// Thông báo lỗi OAuth
app.get('/auth/failure', (req, res) => {
  res.send('Authentication failed');
});

// Route bảo vệ (chỉ web-flow)
function isLoggedIn(req, res, next) {
  req.user ? next() : res.status(401).send('Chưa đăng nhập');
}
app.get('/protected', isLoggedIn, (req, res) => {
  res.send(`Welcome, ${req.user.name}!`);
});

// Logout
app.get('/logout', (req, res, next) => {
  req.logout(err => {
    if (err) return next(err);
    req.session.destroy(() => {
      res.clearCookie('connect.sid');
      res.redirect('/');
    });
  });
});

// 4. API-flow (JSON) – Register/Login thông thường, Google mobile + web-API
app.use('/api/auth', authRoutes);

// 5. Android mobile sign-in (idToken flow)
app.post('/api/auth/google/mobile', loginWithGoogleMobile);

// 6. Start server
app.listen(PORT, () => {
  console.log(`Server running at http://localhost:${PORT}`);
});
