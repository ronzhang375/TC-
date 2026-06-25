import request from '@/utils/request'

export const getOrderList = (params) => request.get('/admin/order/list', { params })
export const getOrderDetail = (id) => request.get(`/admin/order/${id}`)
export const closeOrder = (id) => request.put(`/admin/order/${id}/close`)
