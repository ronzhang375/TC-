import request from '@/utils/request'

export const getProductList = (params) => request.get('/admin/product/list', { params })
export const getProductDetail = (id) => request.get(`/admin/product/${id}`)
export const addProduct = (data) => request.post('/admin/product', data)
export const updateProduct = (id, data) => request.put(`/admin/product/${id}`, data)
export const deleteProduct = (id) => request.delete(`/admin/product/${id}`)
export const updateSaleStatus = (id, status) => request.put(`/admin/product/${id}/sale`, null, { params: { status } })

export const getProductSpecs = (id) => request.get(`/admin/product/${id}/specs`)
export const saveProductSpecs = (id, specs) => request.post(`/admin/product/${id}/specs`, specs)
