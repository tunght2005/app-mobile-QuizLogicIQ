import express from 'express'
import passport from '../middlewares/passport.js'
import { register, login } from '../controllers/authController.js'
import { loginWithGoogleMobile } from '../controllers/googleMobileController.js'

const router = express.Router()

// Email/Password
router.post('/register', register)
router.post('/login', login)

// Android Mobile ID-token flow
router.post('/google/mobile', loginWithGoogleMobile)

// Web OAuth2
router.get(
  '/google',
  passport.authenticate('google', { scope: ['profile', 'email'] })
)
router.get(
  '/google/callback',
  passport.authenticate('google', { failureRedirect: '/auth/failure' }),
  (req, res) => res.redirect('/auth/protected')
)

router.get('/failure', (req, res) => {
  res.send('Google auth failed')
})

// Protected page (web)
function isLoggedIn(req, res, next) {
  req.user ? next() : res.status(401).send('Not logged in')
}
router.get('/protected', isLoggedIn, (req, res) => {
  res.send(`Hello ${req.user.name}`)
})

router.get('/logout', (req, res, next) => {
  req.logout(err => {
    if (err) return next(err)
    req.session.destroy(() => {
      res.clearCookie('connect.sid')
      res.redirect('/')
    })
  })
})

export default router
