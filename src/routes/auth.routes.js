const express = require('express');
const router = express.Router();
const authController = require('../controllers/auth.controller');

// console.log('Auth methods:', Object.keys(authController));

router.post('/google-login', authController.loginWithGoogle);
router.post('/email/register', authController.registerWithEmail);
router.post('/email/login', authController.loginWithEmail);
router.post('/forgot-password', authController.resetPassword);
router.get('/verify-email', authController.verifyEmail);

module.exports = router;
