import request from '@/utils/request'

export const getChannelList = (params) => request.get('/admin/channel/list', { params })
export const getChannelDetail = (id) => request.get(`/admin/channel/${id}`)
export const addChannel = (data) => request.post('/admin/channel', data)
export const updateChannel = (id, data) => request.put(`/admin/channel/${id}`, data)
export const updateChannelStatus = (id, status) => request.put(`/admin/channel/${id}/status`, null, { params: { status } })
export const generateQrCode = (id) => request.post(`/admin/channel/${id}/qrcode`)
