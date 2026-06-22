import { defineStore } from 'pinia'
import { getCartList, getCartCount, addToCart, updateCartQuantity, deleteCartItem } from '../api/cart'

export const useCartStore = defineStore('cart', {
  state: () => ({
    items: [],
    totalCount: 0,
    selectedIds: []
  }),
  getters: {
    totalAmount(state) {
      return state.items
        .filter(item => state.selectedIds.includes(item.cartId))
        .reduce((sum, item) => sum + (item.price * item.quantity), 0)
        .toFixed(2)
    },
    selectedCount(state) {
      return state.items
        .filter(item => state.selectedIds.includes(item.cartId))
        .reduce((sum, item) => sum + item.quantity, 0)
    },
    isAllSelected(state) {
      return state.items.length > 0 && state.selectedIds.length === state.items.length
    }
  },
  actions: {
    async fetchCartList() {
      const res = await getCartList()
      if (res.code === 200) {
        this.items = res.data || []
        this.totalCount = this.items.reduce((sum, item) => sum + item.quantity, 0)
        this.selectedIds = this.items.map(item => item.cartId)
      }
    },
    async fetchCount() {
      const res = await getCartCount()
      if (res.code === 200) this.totalCount = res.data || 0
    },
    async addToCartAction(productId, specId, quantity = 1) {
      await addToCart(productId, specId, quantity)
      await this.fetchCount()
    },
    async updateQuantity(cartId, quantity) {
      await updateCartQuantity(cartId, quantity)
      await this.fetchCartList()
    },
    async deleteItem(cartId) {
      await deleteCartItem(cartId)
      await this.fetchCartList()
    },
    toggleSelect(cartId) {
      const i = this.selectedIds.indexOf(cartId)
      if (i > -1) this.selectedIds.splice(i, 1)
      else this.selectedIds.push(cartId)
    },
    toggleAllSelect() {
      this.selectedIds = this.isAllSelected ? [] : this.items.map(item => item.cartId)
    },
    clearSelected() {
      this.items = this.items.filter(item => !this.selectedIds.includes(item.cartId))
      this.selectedIds = []
    }
  }
})
