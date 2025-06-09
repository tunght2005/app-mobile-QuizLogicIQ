/* eslint-disable no-undef */
// eslint-disable-next-line no-unused-vars
const quizSchema = new mongoose.Schema({
  title: { type: String, required: true },
  description: { type: String, required: true },
  questions: [questionSchema],
  createdBy: { type: mongoose.Schema.Type.Schema.ObjectId, ref: 'User' },
  timesTaken: { type: Number, default: 0 }
}, {
  timestamps: true
})

const questionSchema = new mongoose.Schema({
  questionText: { type: String, required: true },
  options: [optionSchema]
}, {
  timestamps: true
})

const optionSchema = new mongoose.Schema( {
  text: { type: String, required: true },
  isCorrect: { type: Boolean, default: false }
})