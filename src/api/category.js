import request from '@/utils/request'

export const getCategoryTree = () => request.get('/admin/category/tree')
export const getAllCategories = () => request.get('/admin/category/all')
export const addCategory = (data) => request.post('/admin/category', data)
export const updateCategory = (id, data) => request.put(`/admin/category/${id}`, data)
export const deleteCategory = (id) => request.delete(`/admin/category/${id}`)
