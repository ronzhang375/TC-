import request from '@/utils/request'

export const getSupplierList = (params) => request.get('/admin/supplier/list', { params })
export const getSupplierDetail = (id) => request.get(`/admin/supplier/${id}`)
export const addSupplier = (data) => request.post('/admin/supplier', data)
export const updateSupplier = (id, data) => request.put(`/admin/supplier/${id}`, data)
export const updateSupplierStatus = (id, status) => request.put(`/admin/supplier/${id}/status`, null, { params: { status } })
