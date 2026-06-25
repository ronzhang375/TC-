import { defineStore } from 'pinia'
import { wxLogin, getUserInfo } from '../api/user'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: '',
    userInfo: {},
    isLoggedIn: false
  }),
  actions: {
    checkLogin() {
      const token = uni.getStorageSync('token')
      const userInfo = uni.getStorageSync('userInfo')
      if (token && userInfo) {
        this.token = token
        this.userInfo = userInfo
        this.isLoggedIn = true
      }
    },
    async wxLoginAction() {
      const loginRes = await uni.login({ provider: 'weixin' })
      if (!loginRes.code) throw new Error('获取code失败')
      const res = await wxLogin({ code: loginRes.code, nickName: '', avatar: '' })
      if (res.code === 200) {
        this.token = res.data.token
        this.isLoggedIn = true
        await this.fetchUserInfo()
        return res.data
      }
      throw new Error(res.msg)
    },
    async fetchUserInfo() {
      const res = await getUserInfo()
      if (res.code === 200) {
        this.userInfo = res.data
        uni.setStorageSync('userInfo', res.data)
      }
    },
    logout() {
      this.token = ''
      this.userInfo = {}
      this.isLoggedIn = false
      uni.removeStorageSync('token')
      uni.removeStorageSync('userInfo')
    }
  }
})
