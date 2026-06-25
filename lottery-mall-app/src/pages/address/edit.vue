<template>
  <view class="edit-page">
    <view class="form-item"><text class="label">收货人</text><input class="input" v-model="form.consigneeName" placeholder="请输入收货人姓名" /></view>
    <view class="form-item"><text class="label">手机号</text><input class="input" v-model="form.consigneePhone" placeholder="请输入手机号" type="number" maxlength="11" /></view>
    <view class="form-item"><text class="label">所在地区</text><view class="picker" @click="showPicker = true"><text>{{ regionText || '请选择省/市/区' }}</text></view></view>
    <view class="form-item"><text class="label">详细地址</text><textarea class="textarea" v-model="form.detailAddress" placeholder="街道、楼栋号、门牌号等" /></view>
    <view class="bottom-bar"><view class="save-btn" @click="save">保存地址</view></view>
    <u-picker v-if="showPicker" :show="showPicker" :columns="regionColumns" keyName="name" @confirm="onPickerConfirm" @cancel="showPicker = false" />
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { addAddress } from '../../api/user'

const form = ref({ consigneeName: '', consigneePhone: '', provinceCode: '', provinceName: '', cityCode: '', cityName: '', districtCode: '', districtName: '', detailAddress: '', isDefault: 0 })
const showPicker = ref(false)
const regionText = computed(() => { const { provinceName, cityName, districtName } = form.value; return provinceName && cityName && districtName ? `${provinceName} ${cityName} ${districtName}` : '' })
const regionColumns = ref([
  [{ name: '北京市', code: '110000' }, { name: '上海市', code: '310000' }, { name: '福建省', code: '350000' }],
  [{ name: '福州市', code: '350100' }, { name: '厦门市', code: '350200' }, { name: '莆田市', code: '350300' }],
  [{ name: '仓山区', code: '350104' }, { name: '鼓楼区', code: '350102' }, { name: '晋安区', code: '350111' }]
])

function onPickerConfirm(e) {
  const { value } = e
  if (value[0]) { form.value.provinceCode = value[0].code; form.value.provinceName = value[0].name }
  if (value[1]) { form.value.cityCode = value[1].code; form.value.cityName = value[1].name }
  if (value[2]) { form.value.districtCode = value[2].code; form.value.districtName = value[2].name }
  showPicker.value = false
}

async function save() {
  if (!form.value.consigneeName) return uni.showToast({ title: '请输入收货人', icon: 'none' })
  if (!form.value.consigneePhone) return uni.showToast({ title: '请输入手机号', icon: 'none' })
  if (!regionText.value) return uni.showToast({ title: '请选择地区', icon: 'none' })
  if (!form.value.detailAddress) return uni.showToast({ title: '请输入详细地址', icon: 'none' })
  await addAddress(form.value)
  uni.showToast({ title: '保存成功', icon: 'success' })
  setTimeout(() => uni.navigateBack(), 1000)
}
</script>

<style lang="scss" scoped>
.edit-page { min-height: 100vh; background-color: #f5f5f5; padding-bottom: 120rpx; }
.form-item { background-color: #ffffff; padding: 30rpx 20rpx; margin-bottom: 2rpx; display: flex; align-items: center; }
.label { width: 180rpx; font-size: 30rpx; }
.input { flex: 1; font-size: 30rpx; }
.textarea { flex: 1; height: 100rpx; font-size: 30rpx; }
.picker { flex: 1; font-size: 30rpx; }
.bottom-bar { position: fixed; bottom: 0; left: 0; right: 0; padding: 20rpx; background-color: #ffffff; }
.save-btn { background-color: #e54d42; color: #ffffff; height: 90rpx; line-height: 90rpx; text-align: center; border-radius: 45rpx; font-size: 32rpx; }
</style>