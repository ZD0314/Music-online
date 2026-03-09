<template>
  <div class="user-center-page">
    <el-tabs v-model="activeTab">
      <!-- 个人信息 -->
      <el-tab-pane label="个人信息" name="info">
        <el-card>
          <el-form :model="userForm" :rules="userRules" ref="userFormRef" label-width="100px">
            <el-form-item label="头像">
              <el-upload
                class="avatar-uploader"
                :action="`${baseURL}/admin/upload/image`"
                :headers="{ Authorization: `Bearer ${token}` }"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
              >
                <img v-if="userForm.avatar" :src="getImageUrl(userForm.avatar)" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
              </el-upload>
            </el-form-item>

            <el-form-item label="用户名">
              <el-input v-model="userForm.username" disabled />
            </el-form-item>

            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="userForm.nickname" placeholder="请输入昵称" />
            </el-form-item>

            <el-form-item label="性别">
              <el-radio-group v-model="userForm.gender">
                <el-radio :label="0">未知</el-radio>
                <el-radio :label="1">男</el-radio>
                <el-radio :label="2">女</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="生日">
              <el-date-picker
                v-model="userForm.birthday"
                type="date"
                placeholder="选择日期"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>

            <el-form-item label="手机号" prop="phone">
              <el-input v-model="userForm.phone" placeholder="请输入手机号" />
            </el-form-item>

            <el-form-item label="邮箱" prop="email">
              <el-input v-model="userForm.email" placeholder="请输入邮箱" />
            </el-form-item>

            <el-form-item label="个人简介">
              <el-input
                v-model="userForm.bio"
                type="textarea"
                :rows="4"
                placeholder="请输入个人简介"
              />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleUpdateInfo" :loading="loading">
                保存修改
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>

      <!-- 修改密码 -->
      <el-tab-pane label="修改密码" name="password">
        <el-card>
          <el-form
            :model="passwordForm"
            :rules="passwordRules"
            ref="passwordFormRef"
            label-width="100px"
            style="max-width: 500px"
          >
            <el-form-item label="旧密码" prop="oldPassword">
              <el-input
                v-model="passwordForm.oldPassword"
                type="password"
                placeholder="请输入旧密码"
                show-password
              />
            </el-form-item>

            <el-form-item label="新密码" prop="newPassword">
              <el-input
                v-model="passwordForm.newPassword"
                type="password"
                placeholder="请输入新密码"
                show-password
              />
            </el-form-item>

            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input
                v-model="passwordForm.confirmPassword"
                type="password"
                placeholder="请再次输入新密码"
                show-password
              />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleUpdatePassword" :loading="loading">
                修改密码
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>

      <!-- 我的歌单 -->
      <el-tab-pane label="我的歌单" name="playlists">
        <el-card>
          <div class="playlist-header">
            <el-button type="primary" @click="showCreateDialog">创建歌单</el-button>
          </div>

          <el-table :data="playlists" v-loading="loading" style="width: 100%">
            <el-table-column label="封面" width="100">
              <template #default="{ row }">
                <img :src="getImageUrl(row.cover)" class="cover" />
              </template>
            </el-table-column>
            <el-table-column prop="name" label="歌单名称" />
            <el-table-column prop="description" label="描述" />
            <el-table-column prop="songCount" label="歌曲数" width="100" />
            <el-table-column prop="playCount" label="播放次数" width="120" />
            <el-table-column label="操作" width="200">
              <template #default="{ row }">
                <el-button type="text" @click="handleEditPlaylist(row)">编辑</el-button>
                <el-button type="text" @click="handleDeletePlaylist(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>
    </el-tabs>

    <!-- 创建/编辑歌单对话框 -->
    <el-dialog
      :title="playlistDialogTitle"
      v-model="playlistDialogVisible"
      width="500px"
    >
      <el-form :model="playlistForm" :rules="playlistRules" ref="playlistFormRef" label-width="80px">
        <el-form-item label="歌单名称" prop="name">
          <el-input v-model="playlistForm.name" placeholder="请输入歌单名称" />
        </el-form-item>

        <el-form-item label="封面">
          <el-upload
            class="cover-uploader"
            :action="`${baseURL}/admin/upload/image`"
            :headers="{ Authorization: `Bearer ${token}` }"
            :show-file-list="false"
            :on-success="handleCoverSuccess"
          >
            <img v-if="playlistForm.cover" :src="getImageUrl(playlistForm.cover)" class="cover-preview" />
            <el-icon v-else class="cover-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item label="风格">
          <el-input v-model="playlistForm.style" placeholder="请输入歌单风格" />
        </el-form-item>

        <el-form-item label="描述">
          <el-input
            v-model="playlistForm.description"
            type="textarea"
            :rows="4"
            placeholder="请输入歌单描述"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="playlistDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSavePlaylist" :loading="loading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import {
  getUserInfo,
  updateUserInfo,
  updatePassword,
  getMyPlaylistList,
  createUserPlaylist,
  updateUserPlaylist,
  deleteUserPlaylist
} from '@/api'
import { getImageUrl } from '@/utils/format'
import { useUserStore } from '@/stores/user'
import type { Playlist } from '@/types'

const userStore = useUserStore()
const baseURL = 'http://localhost:8080/api'
const token = userStore.token

const activeTab = ref('info')
const loading = ref(false)

// 个人信息表单
const userFormRef = ref<FormInstance>()
const userForm = reactive({
  username: '',
  nickname: '',
  gender: 0,
  birthday: '',
  phone: '',
  email: '',
  avatar: '',
  bio: ''
})

const userRules: FormRules = {
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ]
}

// 修改密码表单
const passwordFormRef = ref<FormInstance>()
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateConfirmPassword = (_rule: any, value: any, callback: any) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const passwordRules: FormRules = {
  oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

// 歌单管理
const playlists = ref<Playlist[]>([])
const playlistDialogVisible = ref(false)
const playlistDialogTitle = ref('创建歌单')
const playlistFormRef = ref<FormInstance>()
const playlistForm = reactive({
  id: null as number | null,
  name: '',
  cover: '',
  style: '',
  description: ''
})

const playlistRules: FormRules = {
  name: [{ required: true, message: '请输入歌单名称', trigger: 'blur' }]
}

// 获取用户信息
const fetchUserInfo = async () => {
  try {
    const data = await getUserInfo()
    Object.assign(userForm, data)
  } catch (error) {
    ElMessage.error('获取用户信息失败')
  }
}

// 更新用户信息
const handleUpdateInfo = async () => {
  if (!userFormRef.value) return

  await userFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await updateUserInfo(userForm)
        ElMessage.success('保存成功')
        // 更新store中的用户信息
        if (userStore.userInfo) {
          userStore.setUserInfo({
            ...userStore.userInfo,
            nickname: userForm.nickname,
            avatar: userForm.avatar
          })
        }
      } catch (error) {
        ElMessage.error('保存失败')
      } finally {
        loading.value = false
      }
    }
  })
}

// 修改密码
const handleUpdatePassword = async () => {
  if (!passwordFormRef.value) return

  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await updatePassword({
          oldPassword: passwordForm.oldPassword,
          newPassword: passwordForm.newPassword
        })
        ElMessage.success('密码修改成功，请重新登录')
        // 清空表单
        passwordForm.oldPassword = ''
        passwordForm.newPassword = ''
        passwordForm.confirmPassword = ''
        // 退出登录
        setTimeout(() => {
          userStore.logout()
        }, 1500)
      } catch (error) {
        ElMessage.error('密码修改失败')
      } finally {
        loading.value = false
      }
    }
  })
}

// 获取歌单列表
const fetchPlaylists = async () => {
  loading.value = true
  try {
    playlists.value = await getMyPlaylistList()
  } catch (error) {
    ElMessage.error('获取歌单列表失败')
  } finally {
    loading.value = false
  }
}

// 显示创建对话框
const showCreateDialog = () => {
  playlistDialogTitle.value = '创建歌单'
  playlistForm.id = null
  playlistForm.name = ''
  playlistForm.cover = ''
  playlistForm.style = ''
  playlistForm.description = ''
  playlistDialogVisible.value = true
}

// 编辑歌单
const handleEditPlaylist = (row: Playlist) => {
  playlistDialogTitle.value = '编辑歌单'
  playlistForm.id = row.id
  playlistForm.name = row.name
  playlistForm.cover = row.cover || ''
  playlistForm.style = row.tags || ''
  playlistForm.description = row.description || ''
  playlistDialogVisible.value = true
}

// 保存歌单
const handleSavePlaylist = async () => {
  if (!playlistFormRef.value) return

  await playlistFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const data = {
          name: playlistForm.name,
          cover: playlistForm.cover,
          style: playlistForm.style,
          description: playlistForm.description
        }

        if (playlistForm.id) {
          await updateUserPlaylist(playlistForm.id, data)
          ElMessage.success('更新成功')
        } else {
          await createUserPlaylist(data)
          ElMessage.success('创建成功')
        }

        playlistDialogVisible.value = false
        fetchPlaylists()
      } catch (error) {
        ElMessage.error('操作失败')
      } finally {
        loading.value = false
      }
    }
  })
}

// 删除歌单
const handleDeletePlaylist = async (row: Playlist) => {
  try {
    await ElMessageBox.confirm('确定要删除这个歌单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    loading.value = true
    await deleteUserPlaylist(row.id)
    ElMessage.success('删除成功')
    fetchPlaylists()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  } finally {
    loading.value = false
  }
}

// 头像上传成功
const handleAvatarSuccess = (response: any) => {
  if (response.code === 200) {
    userForm.avatar = response.data.fileUrl || response.data
    ElMessage.success('头像上传成功')
  } else {
    ElMessage.error('头像上传失败')
  }
}

// 封面上传成功
const handleCoverSuccess = (response: any) => {
  if (response.code === 200) {
    playlistForm.cover = response.data.fileUrl || response.data
    ElMessage.success('封面上传成功')
  } else {
    ElMessage.error('封面上传失败')
  }
}

onMounted(() => {
  fetchUserInfo()
  fetchPlaylists()
})
</script>

<style scoped>
.user-center-page {
  padding: 20px;
}

.playlist-header {
  margin-bottom: 20px;
}

.cover {
  width: 60px;
  height: 60px;
  border-radius: 4px;
  object-fit: cover;
}

.avatar-uploader,
.cover-uploader {
  display: inline-block;
}

.avatar-uploader :deep(.el-upload),
.cover-uploader :deep(.el-upload) {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: border-color 0.3s;
}

.avatar-uploader :deep(.el-upload):hover,
.cover-uploader :deep(.el-upload):hover {
  border-color: #409eff;
}

.avatar-uploader-icon,
.cover-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  text-align: center;
  line-height: 100px;
}

.avatar {
  width: 100px;
  height: 100px;
  display: block;
  border-radius: 50%;
  object-fit: cover;
}

.cover-preview {
  width: 100px;
  height: 100px;
  display: block;
  object-fit: cover;
}
</style>
