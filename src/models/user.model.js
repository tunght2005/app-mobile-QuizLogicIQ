const mongoose = require('mongoose');

const userSchema = new mongoose.Schema({
  email: {
    type: String,
    required: true,
    unique: true,
    trim: true,
    lowercase: true
  },
  password: {
    type: String
  },
  name: {
    type: String
  },
  firebaseUid: {
    type: String,
    unique: true,
    sparse: true,
    default: undefined
  },
  isVerified: {
    type: Boolean,
    default: false
  },
  verificationToken:{
    type: String,
    default: undefined
  },
  loginMethods: {
    type: [String],
    enum: ['google', 'email'],
    default: ['email']
  },
  resetPasswordToken: String,
  resetPasswordExpires: Date,
  createdAt: {
    type: Date,
    default: Date.now
  }
});

module.exports = mongoose.model('User', userSchema); 