import request from '@/utils/request'

export const getBillList = (params) => request.get('/admin/bill/list', { params })
export const getBillDetail = (id) => request.get(`/admin/bill/${id}`)
export const getBillDetails = (id) => request.get(`/admin/bill/${id}/details`)
export const confirmBill = (id) => request.put(`/admin/bill/${id}/confirm`)
export const settleBill = (id) => request.put(`/admin/bill/${id}/settle`)
export const generateWeeklyBills = () => request.post('/admin/bill/generate/weekly')
export const generateMonthlyBills = () => request.post('/admin/bill/generate/monthly')
