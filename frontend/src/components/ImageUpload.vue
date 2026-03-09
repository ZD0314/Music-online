<template>
  <div class="image-upload">
    <el-upload
      class="image-uploader"
      :show-file-list="false"
      :http-request="handleUpload"
      :before-upload="beforeUpload"
      :disabled="disabled"
    >
      <img v-if="imageUrl" :src="getImageUrl(imageUrl)" class="uploaded-image" />
      <div v-else class="upload-placeholder">
        <el-icon class="upload-icon"><Plus /></el-icon>
        <div class="upload-text">{{ placeholder }}</div>
      </div>
    </el-upload>
    <div v-if="showTip" class="upload-tip">
      {{ tip }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { uploadImage } from '@/api'

interface Props {
  modelValue?: string
  width?: number
  height?: number
  maxSize?: number // MB
  placeholder?: string
  tip?: string
  showTip?: boolean
  disabled?: boolean
}

interface Emits {
  (e: 'update:modelValue', value: string): void
  (e: 'success', url: string): void
  (e: 'error', error: any): void
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: '',
  width: 100,
  height: 100,
  maxSize: 5,
  placeholder: '上传图片',
  tip: '支持 jpg、png、gif、webp 格式，大小不超过 5MB',
  showTip: true,
  disabled: false
})

const emit = defineEmits<Emits>()

const imageUrl = computed(() => props.modelValue)

const beforeUpload = (file: File) => {
  const isImage = file.type.startsWith('image/')
  const isLtMaxSize = file.size / 1024 / 1024 < props.maxSize

  if (!isImage) {
    ElMessage.error('只能上传图片文件')
    return false
  }
  if (!isLtMaxSize) {
    ElMessage.error(`图片大小不能超过 ${props.maxSize}MB`)
    return false
  }
  return true
}

const handleUpload = async (options: any) => {
  try {
    const res = await uploadImage(options.file)
    emit('update:modelValue', res.fileUrl)
    emit('success', res.fileUrl)
    ElMessage.success('图片上传成功')
    options.onSuccess(res)
  } catch (error) {
    emit('error', error)
    ElMessage.error('图片上传失败')
    options.onError(error)
  }
}

const getImageUrl = (url: string) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return `http://localhost:8080/api${url}`
}
</script>

<style scoped>
.image-upload {
  display: inline-block;
}

.image-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  overflow: hidden;
  transition: border-color 0.3s;
}

.image-uploader:hover {
  border-color: #409eff;
}

.upload-placeholder {
  width: v-bind('props.width + "px"');
  height: v-bind('props.height + "px"');
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #fafafa;
}

.upload-icon {
  font-size: 28px;
  color: #8c939d;
  margin-bottom: 8px;
}

.upload-text {
  font-size: 12px;
  color: #8c939d;
}

.uploaded-image {
  width: v-bind('props.width + "px"');
  height: v-bind('props.height + "px"');
  display: block;
  object-fit: cover;
}

.upload-tip {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
  line-height: 1.5;
}
</style>
