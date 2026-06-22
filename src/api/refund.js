import request from '@/utils/request'

export const getRefundList = (params) => request.get('/admin/refund/list', { params })
export const getRefundDetail = (id) => request.get(`/admin/refund/${id}`)
export const approveRefund = (id) => request.put(`/admin/refund/${id}/approve`)
export const rejectRefund = (id, reason) => request.put(`/admin/refund/${id}/reject`, null, { params: { reason } })
export const completeRefund = (id) => request.put(`/admin/refund/${id}/complete`)
