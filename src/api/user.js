import { get, post, put, del } from '../utils/request'

export const wxLogin = (data) => post('/app/user/wx-login', data)
export const getUserInfo = () => get('/app/user/info')
export const updateUserInfo = (data) => put('/app/user/info', data)

export const getAddressList = () => get('/app/address/list')
export const getDefaultAddress = () => get('/app/address/default')
export const addAddress = (data) => post('/app/address', data)
export const updateAddress = (id, data) => put(`/app/address/${id}`, data)
export const deleteAddress = (id) => del(`/app/address/${id}`)
export const setDefaultAddress = (id) => put(`/app/address/${id}/default`)
