import request from '@/utils/request'

export const getBannerList = (params) => request.get('/admin/banner/list', { params })
export const addBanner = (data) => request.post('/admin/banner', data)
export const updateBanner = (id, data) => request.put(`/admin/banner/${id}`, data)
export const deleteBanner = (id) => request.delete(`/admin/banner/${id}`)
export const updateBannerStatus = (id, status) => request.put(`/admin/banner/${id}/status`, null, { params: { status } })
