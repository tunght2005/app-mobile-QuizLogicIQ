// eslint-disable-next-line no-unused-vars
const errorHandler = (err, req, res, next ) => {
  // eslint-disable-next-line no-console
  console.error(err.stack)

  const statusCode = res.statusCode || 500
  const message = err.message || 'Internal Server Error'

  res.status(statusCode).json({
    success: false,
    message
  })
}

export default errorHandler