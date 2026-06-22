<script>
import { useUserStore } from './store/user'
import { useCartStore } from './store/cart'

export default {
  onLaunch: function () {
    const userStore = useUserStore()
    userStore.checkLogin()
    if (userStore.isLoggedIn) {
      const cartStore = useCartStore()
      cartStore.fetchCount()
    }
  }
}
</script>

<style lang="scss">
@import "uview-plus/index.scss";

page {
  background-color: #f5f5f5;
}

.price {
  color: #e54d42;
  font-weight: bold;
}
</style>