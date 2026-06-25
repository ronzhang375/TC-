import { get } from '../utils/request'

export const getProductList = (params) => get('/app/product/list', params)
export const getProductDetail = (id) => get(`/app/product/${id}`)
export const getCategoryTree = () => get('/app/category/tree')
export const getAllCategories = () => get('/app/category/all')
