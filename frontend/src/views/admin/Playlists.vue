<template>
  <div class="playlists">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>歌单列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            添加歌单
          </el-button>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="歌单名称">
          <el-input
            v-model="searchForm.name"
            placeholder="请输入歌单名称"
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="封面" width="100">
          <template #default="{ row }">
            <el-avatar :src="getImageUrl(row.cover)" :size="50" shape="square">
              {{ row.name.charAt(0) }}
            </el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="歌单名称" />
        <el-table-column prop="creatorName" label="创建者" width="120" />
        <el-table-column prop="songCount" label="歌曲数" width="100" />
        <el-table-column prop="playCount" label="播放次数" width="120" />
        <el-table-column prop="collectCount" label="收藏次数" width="120" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleManageSongs(row)">
              管理歌曲
            </el-button>
            <el-button type="primary" size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadPlaylists"
        @current-change="loadPlaylists"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <!-- 创建/编辑歌单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="歌单名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入歌单名称" />
        </el-form-item>
        <el-form-item label="封面" prop="cover">
          <ImageUpload
            v-model="form.cover"
            :width="100"
            :height="100"
            placeholder="上传封面"
          />
        </el-form-item>
        <el-form-item label="歌单描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="4"
            placeholder="请输入歌单描述"
          />
        </el-form-item>
        <el-form-item label="创建者" prop="creatorType">
          <el-radio-group v-model="form.creatorType">
            <el-radio :label="1">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
          确定
        </el-button>
      </template>
    </el-dialog>

    <!-- 管理歌曲对话框 -->
    <el-dialog
      v-model="songDialogVisible"
      title="管理歌单歌曲"
      width="900px"
      @close="handleSongDialogClose"
    >
      <div class="song-management">
        <div class="add-song-section">
          <h3>添加歌曲</h3>
          <el-form :inline="true" :model="songSearchForm">
            <el-form-item label="歌曲名称">
              <el-input
                v-model="songSearchForm.name"
                placeholder="请输入歌曲名称"
                clearable
              />
            </el-form-item>
            <el-form-item label="歌手">
              <el-select
                v-model="songSearchForm.artistId"
                placeholder="请选择歌手"
                clearable
                filterable
                style="width: 200px"
              >
                <el-option
                  v-for="artist in artistList"
                  :key="artist.id"
                  :label="artist.name"
                  :value="artist.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearchSongs">查询</el-button>
            </el-form-item>
          </el-form>

          <el-table :data="availableSongs" border stripe max-height="300">
            <el-table-column prop="name" label="歌曲名称" />
            <el-table-column prop="artistName" label="歌手" width="150" />
            <el-table-column prop="albumName" label="专辑" width="150" />
            <el-table-column label="操作" width="100">
              <template #default="{ row }">
                <el-button
                  type="primary"
                  size="small"
                  @click="handleAddSong(row)"
                  :disabled="isInPlaylist(row.id)"
                >
                  {{ isInPlaylist(row.id) ? '已添加' : '添加' }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            v-model:current-page="songPagination.page"
            v-model:page-size="songPagination.size"
            :total="songPagination.total"
            :page-sizes="[10, 20, 50]"
            layout="total, sizes, prev, pager, next"
            @size-change="loadAvailableSongs"
            @current-change="loadAvailableSongs"
            style="margin-top: 10px"
          />
        </div>

        <el-divider />

        <div class="playlist-songs-section">
          <h3>歌单歌曲列表</h3>
          <el-table :data="playlistSongs" border stripe max-height="300">
            <el-table-column prop="name" label="歌曲名称" />
            <el-table-column prop="artistName" label="歌手" width="150" />
            <el-table-column prop="albumName" label="专辑" width="150" />
            <el-table-column label="操作" width="100">
              <template #default="{ row }">
                <el-button
                  type="danger"
                  size="small"
                  @click="handleRemoveSong(row)"
                >
                  移除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import {
  getPlaylistList,
  createPlaylist,
  updatePlaylist,
  deletePlaylist,
  addSongToPlaylist,
  removeSongFromPlaylist,
  getSongList,
  getArtistList
} from '@/api'
import type { Playlist, Song, Artist } from '@/types'
import { ImageUpload } from '@/components'

const tableData = ref<Playlist[]>([])
const artistList = ref<Artist[]>([])
const availableSongs = ref<Song[]>([])
const playlistSongs = ref<Song[]>([])
const dialogVisible = ref(false)
const songDialogVisible = ref(false)
const dialogTitle = ref('')
const submitLoading = ref(false)
const formRef = ref<FormInstance>()
const currentPlaylist = ref<Playlist | null>(null)

const searchForm = reactive({
  name: ''
})

const pagination = reactive({
  page: 1,
  size: 20,
  total: 0
})

const songSearchForm = reactive({
  name: '',
  artistId: undefined as number | undefined
})

const songPagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const form = reactive<Partial<Playlist>>({
  name: '',
  cover: '',
  description: '',
  creatorId: 1,
  creatorType: 1,
  status: 1
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入歌单名称', trigger: 'blur' }]
}

const loadPlaylists = async () => {
  try {
    const res = await getPlaylistList({
      page: pagination.page,
      size: pagination.size,
      name: searchForm.name || undefined
    })
    tableData.value = res.records
    pagination.total = res.total
  } catch (error) {
    ElMessage.error('加载歌单列表失败')
  }
}

const loadArtists = async () => {
  try {
    const res = await getArtistList({
      page: 1,
      size: 1000
    })
    artistList.value = res.records
  } catch (error) {
    ElMessage.error('加载歌手列表失败')
  }
}

const loadAvailableSongs = async () => {
  try {
    const res = await getSongList({
      page: songPagination.page,
      size: songPagination.size,
      name: songSearchForm.name || undefined,
      artistId: songSearchForm.artistId
    })
    availableSongs.value = res.records
    songPagination.total = res.total
  } catch (error) {
    ElMessage.error('加载歌曲列表失败')
  }
}

const loadPlaylistSongs = async (playlistId: number) => {
  try {
    const res = await getSongList({
      page: 1,
      size: 1000
    })
    // 这里简化处理，实际应该有专门的接口获取歌单中的歌曲
    playlistSongs.value = []
  } catch (error) {
    ElMessage.error('加载歌单歌曲失败')
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadPlaylists()
}

const handleReset = () => {
  searchForm.name = ''
  pagination.page = 1
  loadPlaylists()
}

const handleAdd = () => {
  dialogTitle.value = '添加歌单'
  dialogVisible.value = true
}

const handleEdit = (row: Playlist) => {
  dialogTitle.value = '编辑歌单'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row: Playlist) => {
  try {
    await ElMessageBox.confirm('确定要删除该歌单吗？', '提示', {
      type: 'warning'
    })
    await deletePlaylist(row.id)
    ElMessage.success('删除成功')
    loadPlaylists()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleManageSongs = (row: Playlist) => {
  currentPlaylist.value = row
  songDialogVisible.value = true
  loadAvailableSongs()
  loadPlaylistSongs(row.id)
}

const handleSearchSongs = () => {
  songPagination.page = 1
  loadAvailableSongs()
}

const handleAddSong = async (song: Song) => {
  if (!currentPlaylist.value) return

  try {
    await addSongToPlaylist(currentPlaylist.value.id, song.id)
    ElMessage.success('添加成功')
    loadPlaylistSongs(currentPlaylist.value.id)
    loadPlaylists()
  } catch (error) {
    ElMessage.error('添加失败')
  }
}

const handleRemoveSong = async (song: Song) => {
  if (!currentPlaylist.value) return

  try {
    await ElMessageBox.confirm('确定要从歌单中移除该歌曲吗？', '提示', {
      type: 'warning'
    })
    await removeSongFromPlaylist(currentPlaylist.value.id, song.id)
    ElMessage.success('移除成功')
    loadPlaylistSongs(currentPlaylist.value.id)
    loadPlaylists()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error('移除失败')
    }
  }
}

const isInPlaylist = (songId: number) => {
  return playlistSongs.value.some(song => song.id === songId)
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    submitLoading.value = true
    try {
      if (form.id) {
        await updatePlaylist(form.id, form)
        ElMessage.success('更新成功')
      } else {
        await createPlaylist(form)
        ElMessage.success('创建成功')
      }
      dialogVisible.value = false
      loadPlaylists()
    } catch (error) {
      ElMessage.error('操作失败')
    } finally {
      submitLoading.value = false
    }
  })
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
  form.name = ''
  form.cover = ''
  form.description = ''
  form.creatorId = 1
  form.creatorType = 1
  form.status = 1
  delete form.id
}

const handleSongDialogClose = () => {
  currentPlaylist.value = null
  songSearchForm.name = ''
  songSearchForm.artistId = undefined
  songPagination.page = 1
  availableSongs.value = []
  playlistSongs.value = []
}

const getImageUrl = (url: string) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return `http://localhost:8080/api${url}`
}

onMounted(() => {
  loadPlaylists()
  loadArtists()
})
</script>

<style scoped>
.playlists {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.song-management {
  padding: 10px 0;
}

.song-management h3 {
  margin-bottom: 15px;
  font-size: 16px;
  font-weight: 600;
}

.add-song-section,
.playlist-songs-section {
  margin-bottom: 20px;
}
</style>
