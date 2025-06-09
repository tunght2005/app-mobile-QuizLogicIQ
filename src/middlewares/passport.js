// src/middlewares/auth.js
import jwt from 'jsonwebtoken'
import passport from 'passport'
import 'dotenv/config'
import { Strategy as GoogleStrategy } from 'passport-google-oauth2'
import User from '../models/user.js' // chỉnh đường dẫn tuỳ project

// Google OAuth setup
const { GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET, JWT_SECRET } = process.env

passport.use(new GoogleStrategy({
  clientID: GOOGLE_CLIENT_ID,
  clientSecret: GOOGLE_CLIENT_SECRET,
  callbackURL:  'http://localhost:3000/google/callback',
  passReqToCallback: true
}, async (req, accessToken, refreshToken, profile, done) => {
  try {
    // profile.name = { givenName, familyName }
    const fullName = `${profile.name.givenName} ${profile.name.familyName}`

    // profile.email || profile.emails[0].value
    const email = Array.isArray(profile.emails)
      ? profile.emails[0].value
      : profile.email

    // profile.photos is an array for oauth20, for oauth2 use profile.picture
    const avatar = Array.isArray(profile.photos)
      ? profile.photos[0].value
      : profile.picture

    let user = await User.findOne({ googleId: profile.id })
    if (!user) {
      user = await User.create({
        googleId: profile.id,
        name: fullName,
        email,
        avatar
      })
    }
    return done(null, user)
  } catch (err) {
    return done(err, null)
  }
}
))
passport.serializeUser((user, done) => {
  done(null, user._id)
})

passport.deserializeUser(async (id, done) => {
  try {
    const user = await User.findById(id).select('-password')
    done(null, user)
  } catch (err) {
    done(err, null)
  }
})

// JWT-protect middleware
export const protect = async (req, res, next) => {
  try {
    let token
    if (req.headers.authorization?.startsWith('Bearer ')) {
      token = req.headers.authorization.split(' ')[1]
    }
    if (!token) {
      res.status(401)
      return next(new Error('Not authorized, no token'))
    }
    const decoded = jwt.verify(token, JWT_SECRET)
    req.user = await User.findById(decoded.id).select('-password')
    if (!req.user) {
      res.status(401)
      return next(new Error('User not found'))
    }
    next()
  } catch (error) {
    res.status(401)
    next(new Error('Not authorized, token failed'))
  }
}

// Admin-only middleware
export const admin = (req, res, next) => {
  if (!req.user || req.user.role !== 'admin') {
    res.status(403)
    return next(new Error('Access denied, admin only'))
  }
  next()
}

export default passport