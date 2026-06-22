import request from '@/utils/request'

export const getRegionList = () => request.get('/admin/region/list')
export const addRegion = (data) => request.post('/admin/region', data)
export const updateRegion = (id, data) => request.put(`/admin/region/${id}`, data)
export const deleteRegion = (id) => request.delete(`/admin/region/${id}`)
