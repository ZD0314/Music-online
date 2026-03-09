<template>
  <div class="audio-upload">
    <el-upload
      :show-file-list="false"
      :http-request="handleUpload"
      :before-upload="beforeUpload"
      :disabled="disabled || uploading"
    >
      <el-button type="primary" :icon="Upload" :loading="uploading" :disabled="disabled">
        {{ buttonText }}
      </el-button>
    </el-upload>

    <div v-if="uploading" class="upload-progress">
      <el-progress :percentage="uploadProgress" :status="progressStatus" />
      <span class="progress-text">{{ progressText }}</span>
    </div>

    <div v-if="audioUrl && !uploading" class="file-info">
      <el-icon class="success-icon"><CircleCheck /></el-icon>
      <span class="file-name">{{ fileName }}</span>
      <el-button
        v-if="!disabled"
        type="danger"
        size="small"
        text
        @click="handleRemove"
      >
        删除
      </el-button>
    </div>

    <div v-if="showTip" class="upload-tip">
      {{ tip }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Upload, CircleCheck } from '@element-plus/icons-vue'
import { uploadAudio } from '@/api'

interface Props {
  modelValue?: string
  maxSize?: number // MB
  acceptFormats?: string[]
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
  maxSize: 50,
  acceptFormats: () => ['mp3', 'flac', 'wav', 'aac', 'ogg', 'm4a'],
  tip: '支持 mp3、flac、wav、aac、ogg、m4a 格式，大小不超过 50MB',
  showTip: true,
  disabled: false
})

const emit = defineEmits<Emits>()

const uploading = ref(false)
const uploadProgress = ref(0)
const fileName = ref('')

const audioUrl = computed(() => props.modelValue)

const buttonText = computed(() => {
  if (uploading.value) return '上传中...'
  if (audioUrl.value) return '重新上传'
  return '上传音频'
})

const progressStatus = computed(() => {
  if (uploadProgress.value === 100) return 'success'
  return undefined
})

const progressText = computed(() => {
  return `${uploadProgress.value}%`
})

const beforeUpload = (file: File) => {
  const validTypes = ['audio/mpeg', 'audio/flac', 'audio/wav', 'audio/aac', 'audio/ogg', 'audio/mp4']
  const isAudio = validTypes.includes(file.type) ||
    props.acceptFormats.some(format => file.name.toLowerCase().endsWith(`.${format}`))
  const isLtMaxSize = file.size / 1024 / 1024 < props.maxSize

  if (!isAudio) {
    ElMessage.error(`只能上传音频文件（${props.acceptFormats.join('、')}）`)
    return false
  }
  if (!isLtMaxSize) {
    ElMessage.error(`音频文件大小不能超过 ${props.maxSize}MB`)
    return false
  }

  fileName.value = file.name
  return true
}

const handleUpload = async (options: any) => {
  uploading.value = true
  uploadProgress.value = 0

  // 模拟上传进度
  const progressInterval = setInterval(() => {
    if (uploadProgress.value < 90) {
      uploadProgress.value += 10
    }
  }, 200)

  try {
    const res = await uploadAudio(options.file)
    uploadProgress.value = 100

    setTimeout(() => {
      emit('update:modelValue', res.fileUrl)
      emit('success', res.fileUrl)
      ElMessage.success('音频上传成功')
      uploading.value = false
      uploadProgress.value = 0
    }, 500)

    options.onSuccess(res)
  } catch (error) {
    emit('error', error)
    ElMessage.error('音频上传失败')
    uploading.value = false
    uploadProgress.value = 0
    options.onError(error)
  } finally {
    clearInterval(progressInterval)
  }
}

const handleRemove = () => {
  emit('update:modelValue', '')
  fileName.value = ''
}
</script>

<style scoped>
.audio-upload {
  display: inline-block;
  width: 100%;
}

.upload-progress {
  margin-top: 10px;
}

.progress-text {
  font-size: 12px;
  color: #606266;
  margin-left: 10px;
}

.file-info {
  margin-top: 10px;
  padding: 8px 12px;
  background-color: #f0f9ff;
  border: 1px solid #b3d8ff;
  border-radius: 4px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.success-icon {
  color: #67c23a;
  font-size: 16px;
}

.file-name {
  flex: 1;
  font-size: 13px;
  color: #606266;
  word-break: break-all;
}

.upload-tip {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
  line-height: 1.5;
}
</style>
