import mongoose from 'mongoose'
import bcrypt from 'bcryptjs'

const userSchema = new mongoose.Schema ({
  name: { type : String, required: true, minlength: 3, maxlength: 30 },
  email: { type: String, required: true, unique: true, lowercase: true },
  avatar: { type: String, default: 'https://www.gravatar.com/avatar/?d=mp' },
  role: { type: String, enum: ['admin', 'user'], default: 'user' },
  googleId: { type: String, unique: true, sparse: true },
  googleEmail: { type: String, lowercase:true },
  password: { type : String, minlength: 6, required: function() { return !this.googleId } }
}, {
  timestamps: true
})

userSchema.pre('save', async function(next) {
  if (!this.isModified('password')) return next()
  const salt = await bcrypt.genSalt(10)
  this.password = await bcrypt.hash(this.password, salt)
  next()
})

userSchema.methods.matchPassword = async function(enteredPassword) {
  return await bcrypt.compare(enteredPassword, this.password)
}

const User = mongoose.model('User', userSchema)
export default User