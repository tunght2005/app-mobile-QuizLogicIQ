/* eslint-disable no-undef */
const answersSchema = new mongoose.Schema({
  questionId: { type: mongoose.Schema.type.Schema.ObjectId, required: true },
  answerIndex: { type: Number, required: true },
  iscorrect: { type: Boolean, default: false }
})

// eslint-disable-next-line no-unused-vars
const resultSchema = new mongoose.Schema({
  quiz: { type: mongoose.Schema.Types.ObjectId, ref: 'Quiz', required: true },
  user: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
  score: { type: Number, default: 0 },
  totalQuestions: { type: Number, required: true },
  correctCount: { type: Number, required: true },
  answers: [answersSchema],
  takenAt: { type: Date, default: Date.now }
})