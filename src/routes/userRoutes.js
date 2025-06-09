// const express = require('express')
// import authenticateToken from '~/middlewares/auth'
// import authorizeRoles from '~/middlewares/authorizeRole'


// const router = express.Router()


// // Ví dụ route chỉ admin mới được xem danh sách người dùng
// // eslint-disable-next-line no-unused-vars
// router.get('/path', userController.getUser);

// // Route cả 2 có quyền tạo lớp học
// // eslint-disable-next-line no-unused-vars
// router.post('/create-class', authenticateToken, authorizeRoles('admin', 'user'), ( req, res ) => {
//   // logic tạo lớp
// })