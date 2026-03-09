<template>
  <div class="lyric-upload">
    <el-upload
      :show-file-list="false"
      :http-request="handleUpload"
      :before-upload="beforeUpload"
      :disabled="disabled"
    >
      <el-button type="primary" :icon="Upload" :disabled="disabled">
        {{ buttonText }}
      </el-button>
    </el-upload>

    <div v-if="lyricUrl" class="file-info">
      <el-icon class="success-icon"><CircleCheck /></el-icon>
      <span class="file-name">{{ fileName }}</span>
      <el-button
        v-if="!disabled"
        type="primary"
        size="small"
        text
        @click="handlePreview"
      >
        预览
      </el-button>
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

    <!-- 歌词预览对话框 -->
    <el-dialog
      v-model="previewVisible"
      title="歌词预览"
      width="600px"
    >
      <div class="lyric-preview">
        <pre>{{ lyricContent }}</pre>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Upload, CircleCheck } from '@element-plus/icons-vue'
import { uploadLyric } from '@/api'

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
  maxSize: 1,
  acceptFormats: () => ['lrc', 'txt'],
  tip: '支持 lrc、txt 格式，大小不超过 1MB',
  showTip: true,
  disabled: false
})

const emit = defineEmits<Emits>()

const fileName = ref('')
const lyricContent = ref('')
const previewVisible = ref(false)

const lyricUrl = computed(() => props.modelValue)

const buttonText = computed(() => {
  if (lyricUrl.value) return '重新上传'
  return '上传歌词'
})

const beforeUpload = (file: File) => {
  const isLyric = props.acceptFormats.some(format =>
    file.name.toLowerCase().endsWith(`.${format}`)
  )
  const isLtMaxSize = file.size / 1024 / 1024 < props.maxSize

  if (!isLyric) {
    ElMessage.error(`只能上传歌词文件（${props.acceptFormats.join('、')}）`)
    return false
  }
  if (!isLtMaxSize) {
    ElMessage.error(`歌词文件大小不能超过 ${props.maxSize}MB`)
    return false
  }

  fileName.value = file.name

  // 读取文件内容用于预览
  const reader = new FileReader()
  reader.onload = (e) => {
    lyricContent.value = e.target?.result as string
  }
  reader.readAsText(file)

  return true
}

const handleUpload = async (options: any) => {
  try {
    const res = await uploadLyric(options.file)
    emit('update:modelValue', res.fileUrl)
    emit('success', res.fileUrl)
    ElMessage.success('歌词上传成功')
    options.onSuccess(res)
  } catch (error) {
    emit('error', error)
    ElMessage.error('歌词上传失败')
    options.onError(error)
  }
}

const handleRemove = () => {
  emit('update:modelValue', '')
  fileName.value = ''
  lyricContent.value = ''
}

const handlePreview = () => {
  if (lyricContent.value) {
    previewVisible.value = true
  } else {
    ElMessage.warning('暂无歌词内容')
  }
}
</script>

<style scoped>
.lyric-upload {
  display: inline-block;
  width: 100%;
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

.lyric-preview {
  max-height: 500px;
  overflow-y: auto;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.lyric-preview pre {
  margin: 0;
  font-family: 'Courier New', monospace;
  font-size: 13px;
  line-height: 1.8;
  color: #303133;
  white-space: pre-wrap;
  word-wrap: break-word;
}
</style>
