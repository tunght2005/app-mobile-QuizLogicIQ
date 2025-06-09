import mongoose from 'mongoose'

const connectDB = async () => {
  try {
    await mongoose.connect('mongodb://localhost:27017/quiz-app')
    // eslint-disable-next-line no-console
    console.log('MongoDB connected successfully')

  } catch (error) {
    // eslint-disable-next-line no-console
    console.error('mongodb connection error:', error.message)
    process.exit(1)
  }
}
export default connectDB