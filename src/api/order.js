import { get, post, put } from '../utils/request'

export const createOrder = (data) => post('/app/order/create', data)
export const getOrderList = (status, pageNum = 1, pageSize = 10) =>
  get('/app/order/list', { status, pageNum, pageSize })
export const getOrderDetail = (id) => get(`/app/order/${id}`)
export const cancelOrder = (id) => put(`/app/order/${id}/cancel`)
export const confirmReceive = (id) => put(`/app/order/${id}/receive`)

export const applyRefund = (data) => post('/app/refund/apply', data)
export const getRefundList = (pageNum = 1, pageSize = 10) =>
  get('/app/refund/list', { pageNum, pageSize })
