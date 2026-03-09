# 文件上传组件使用文档

## 概述

本项目提供了三个可复用的文件上传组件,用于处理图片、音频和歌词文件的上传。

## 组件列表

### 1. ImageUpload - 图片上传组件

**功能特性:**
- 支持图片预览
- 自定义尺寸
- 文件类型和大小验证
- 支持 jpg、png、gif、webp 格式

**Props:**
| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| modelValue | string | '' | 图片URL (v-model) |
| width | number | 100 | 预览宽度(px) |
| height | number | 100 | 预览高度(px) |
| maxSize | number | 5 | 最大文件大小(MB) |
| placeholder | string | '上传图片' | 占位文本 |
| tip | string | - | 提示文本 |
| showTip | boolean | true | 是否显示提示 |
| disabled | boolean | false | 是否禁用 |

**Events:**
| 事件名 | 参数 | 说明 |
|--------|------|------|
| update:modelValue | (url: string) | 更新图片URL |
| success | (url: string) | 上传成功 |
| error | (error: any) | 上传失败 |

**使用示例:**
```vue
<template>
  <ImageUpload
    v-model="form.avatar"
    :width="100"
    :height="100"
    placeholder="上传头像"
    @success="handleSuccess"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ImageUpload } from '@/components'

const form = ref({
  avatar: ''
})

const handleSuccess = (url: string) => {
  console.log('上传成功:', url)
}
</script>
```

### 2. AudioUpload - 音频上传组件

**功能特性:**
- 上传进度条显示
- 文件信息展示
- 支持多种音频格式
- 支持 mp3、flac、wav、aac、ogg、m4a 格式

**Props:**
| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| modelValue | string | '' | 音频URL (v-model) |
| maxSize | number | 50 | 最大文件大小(MB) |
| acceptFormats | string[] | ['mp3', 'flac', 'wav', 'aac', 'ogg', 'm4a'] | 支持的格式 |
| tip | string | - | 提示文本 |
| showTip | boolean | true | 是否显示提示 |
| disabled | boolean | false | 是否禁用 |

**Events:**
| 事件名 | 参数 | 说明 |
|--------|------|------|
| update:modelValue | (url: string) | 更新音频URL |
| success | (url: string) | 上传成功 |
| error | (error: any) | 上传失败 |

**使用示例:**
```vue
<template>
  <AudioUpload
    v-model="form.audioUrl"
    :max-size="50"
    @success="handleSuccess"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { AudioUpload } from '@/components'

const form = ref({
  audioUrl: ''
})

const handleSuccess = (url: string) => {
  console.log('上传成功:', url)
}
</script>
```

### 3. LyricUpload - 歌词上传组件

**功能特性:**
- 歌词预览功能
- 文件信息展示
- 支持 lrc、txt 格式

**Props:**
| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| modelValue | string | '' | 歌词URL (v-model) |
| maxSize | number | 1 | 最大文件大小(MB) |
| acceptFormats | string[] | ['lrc', 'txt'] | 支持的格式 |
| tip | string | - | 提示文本 |
| showTip | boolean | true | 是否显示提示 |
| disabled | boolean | false | 是否禁用 |

**Events:**
| 事件名 | 参数 | 说明 |
|--------|------|------|
| update:modelValue | (url: string) | 更新歌词URL |
| success | (url: string) | 上传成功 |
| error | (error: any) | 上传失败 |

**使用示例:**
```vue
<template>
  <LyricUpload
    v-model="form.lyricUrl"
    @success="handleSuccess"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { LyricUpload } from '@/components'

const form = ref({
  lyricUrl: ''
})

const handleSuccess = (url: string) => {
  console.log('上传成功:', url)
}
</script>
```

## 完整示例

查看 `src/views/admin/UploadDemo.vue` 文件获取完整的使用示例。

## 后端接口

### 1. 上传图片
```
POST /api/admin/upload/image
Content-Type: multipart/form-data

参数:
- file: 图片文件

响应:
{
  "code": 200,
  "message": "success",
  "data": {
    "fileName": "uuid.jpg",
    "fileUrl": "/uploads/images/uuid.jpg",
    "fileSize": 12345
  }
}
```

### 2. 上传音频
```
POST /api/admin/upload/audio
Content-Type: multipart/form-data

参数:
- file: 音频文件

响应:
{
  "code": 200,
  "message": "success",
  "data": {
    "fileName": "uuid.mp3",
    "fileUrl": "/uploads/audio/uuid.mp3",
    "fileSize": 1234567
  }
}
```

### 3. 上传歌词
```
POST /api/admin/upload/lyric
Content-Type: multipart/form-data

参数:
- file: 歌词文件

响应:
{
  "code": 200,
  "message": "success",
  "data": {
    "fileName": "uuid.lrc",
    "fileUrl": "/uploads/lyrics/uuid.lrc",
    "fileSize": 1234
  }
}
```

## 注意事项

1. **文件大小限制:**
   - 图片: 最大 5MB
   - 音频: 最大 50MB
   - 歌词: 最大 1MB

2. **支持的文件格式:**
   - 图片: jpg、jpeg、png、gif、webp
   - 音频: mp3、flac、wav、aac、ogg、m4a
   - 歌词: lrc、txt

3. **文件存储路径:**
   - 图片: `uploads/images/`
   - 音频: `uploads/audio/`
   - 歌词: `uploads/lyrics/`

4. **URL 处理:**
   - 组件会自动处理相对路径和绝对路径
   - 相对路径会自动添加 API 前缀: `http://localhost:8080/api`

5. **错误处理:**
   - 所有组件都会在上传失败时显示错误提示
   - 可以通过 `@error` 事件监听错误并自定义处理

## 在现有页面中使用

如果要在现有的管理页面(如 Artists.vue、Songs.vue)中使用这些组件,可以替换原有的上传代码:

**替换前:**
```vue
<el-upload
  class="avatar-uploader"
  :show-file-list="false"
  :http-request="uploadAvatar"
  :before-upload="beforeAvatarUpload"
>
  <img v-if="form.avatar" :src="getImageUrl(form.avatar)" class="avatar" />
  <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
</el-upload>
```

**替换后:**
```vue
<ImageUpload
  v-model="form.avatar"
  :width="100"
  :height="100"
  placeholder="上传头像"
/>
```

这样可以大大简化代码,提高可维护性。
