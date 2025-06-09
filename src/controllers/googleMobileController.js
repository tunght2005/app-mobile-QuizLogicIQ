import { OAuth2Client } from "google-auth-library";
import jwt from 'jsonwebtoken'
import User from '../models/user.js'
import 'dotenv/config'

const client = new OAuth2Client(process.env.GOOGLE_CLIENT_ID_ANDROID)

export const loginWithGoogleMobile = async (req, res, next)=>{
  const {idToken} = req.body
  if(!idToken) return res.status(400).json({ message: 'Missing idToken '})
    try{
      const ticket = await client.verifyIdToken({
        idToken,
        audience: process.env.GOOGLE_CLIENT_ID_ANDROID
      })
      const {sub: googleId, email, name, picture: avatar} = ticket.getPayload()

      let user = await User.findOne({ googleId })
      if(!user){
        user = await User.create({ googleId , name, email, avatar})
      }

      const token = jwt.sign({ id: user._id }, process.env.JWT_SECRET, {expiresIn: '7d'})
      res.json({
        _id: user._id,
        name: user.name,
        email: user.email,
        avatar: user.avatar,
        token
      })
    }catch(err){
      next(err)
    }
}