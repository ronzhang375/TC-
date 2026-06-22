import request from '@/utils/request'

export const login = (data) => request.post('/admin/login', data)

export const logout = () => request.post('/admin/logout')
