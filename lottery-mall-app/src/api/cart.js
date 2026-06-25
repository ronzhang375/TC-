import { get, post, put, del } from '../utils/request'

export const getCartList = () => get('/app/cart/list')
export const getCartCount = () => get('/app/cart/count')
export const addToCart = (productId, specId, quantity) =>
  post('/app/cart/add', null, { params: { productId, specId, quantity } })
export const updateCartQuantity = (cartId, quantity) =>
  put(`/app/cart/quantity/${cartId}`, null, { params: { quantity } })
export const deleteCartItem = (cartId) => del(`/app/cart/${cartId}`)
export const clearCart = () => del('/app/cart/clear')
