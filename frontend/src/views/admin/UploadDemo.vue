<template>
  <div class="upload-demo">
    <el-card>
      <template #header>
        <span>文件上传组件示例</span>
      </template>

      <el-form :model="form" label-width="120px">
        <el-divider content-position="left">图片上传组件</el-divider>

        <el-form-item label="头像上传">
          <ImageUpload
            v-model="form.avatar"
            :width="100"
            :height="100"
            placeholder="上传头像"
            @success="handleAvatarSuccess"
          />
        </el-form-item>

        <el-form-item label="封面上传">
          <ImageUpload
            v-model="form.cover"
            :width="150"
            :height="150"
            placeholder="上传封面"
            tip="支持 jpg、png、gif、webp 格式，大小不超过 5MB，建议尺寸 500x500"
            @success="handleCoverSuccess"
          />
        </el-form-item>

        <el-divider content-position="left">音频上传组件</el-divider>

        <el-form-item label="音频文件">
          <AudioUpload
            v-model="form.audioUrl"
            @success="handleAudioSuccess"
          />
        </el-form-item>

        <el-divider content-position="left">歌词上传组件</el-divider>

        <el-form-item label="歌词文件">
          <LyricUpload
            v-model="form.lyricUrl"
            @success="handleLyricSuccess"
          />
        </el-form-item>

        <el-divider />

        <el-form-item>
          <el-button type="primary" @click="handleSubmit">提交</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-divider content-position="left">表单数据</el-divider>

      <el-descriptions :column="1" border>
        <el-descriptions-item label="头像URL">
          {{ form.avatar || '未上传' }}
        </el-descriptions-item>
        <el-descriptions-item label="封面URL">
          {{ form.cover || '未上传' }}
        </el-descriptions-item>
        <el-descriptions-item label="音频URL">
          {{ form.audioUrl || '未上传' }}
        </el-descriptions-item>
        <el-descriptions-item label="歌词URL">
          {{ form.lyricUrl || '未上传' }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { ImageUpload, AudioUpload, LyricUpload } from '@/components'

const form = reactive({
  avatar: '',
  cover: '',
  audioUrl: '',
  lyricUrl: ''
})

const handleAvatarSuccess = (url: string) => {
  console.log('头像上传成功:', url)
}

const handleCoverSuccess = (url: string) => {
  console.log('封面上传成功:', url)
}

const handleAudioSuccess = (url: string) => {
  console.log('音频上传成功:', url)
}

const handleLyricSuccess = (url: string) => {
  console.log('歌词上传成功:', url)
}

const handleSubmit = () => {
  console.log('表单数据:', form)
  ElMessage.success('提交成功')
}

const handleReset = () => {
  form.avatar = ''
  form.cover = ''
  form.audioUrl = ''
  form.lyricUrl = ''
  ElMessage.info('已重置')
}
</script>

<style scoped>
.upload-demo {
  padding: 20px;
}
</style>
