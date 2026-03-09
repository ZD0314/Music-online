<template>
  <div class="songs">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>歌曲列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            添加歌曲
          </el-button>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="歌曲名称">
          <el-input
            v-model="searchForm.name"
            placeholder="请输入歌曲名称"
            clearable
          />
        </el-form-item>
        <el-form-item label="歌手">
          <el-select
            v-model="searchForm.artistId"
            placeholder="请选择歌手"
            clearable
            filterable
            popper-class="custom-select-dropdown"
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
        <el-table-column prop="name" label="歌曲名称" />
        <el-table-column prop="artistName" label="歌手" width="150" />
        <el-table-column prop="albumName" label="专辑" width="150" />
        <el-table-column prop="duration" label="时长" width="100">
          <template #default="{ row }">
            {{ formatDuration(row.duration) }}
          </template>
        </el-table-column>
        <el-table-column prop="playCount" label="播放次数" width="120" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
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
        @size-change="loadSongs"
        @current-change="loadSongs"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
      destroy-on-close
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        style="overflow: visible"
      >
        <el-form-item label="歌曲名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入歌曲名称" />
        </el-form-item>
        <el-form-item label="歌手" prop="artistId">
          <el-select
            v-model="form.artistId"
            placeholder="请选择歌手"
            filterable
            style="width: 100%"
            popper-class="custom-select-dropdown"
            @change="handleArtistChange"
          >
            <el-option
              v-for="artist in artistList"
              :key="artist.id"
              :label="artist.name"
              :value="artist.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="专辑" prop="albumId">
          <el-select
            v-model="form.albumId"
            placeholder="请选择专辑（可选）"
            clearable
            filterable
            style="width: 100%"
            popper-class="custom-select-dropdown"
          >
            <el-option
              v-for="album in albumList"
              :key="album.id"
              :label="album.name"
              :value="album.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="封面" prop="cover">
          <ImageUpload
            v-model="form.cover"
            :width="100"
            :height="100"
            placeholder="上传封面"
          />
        </el-form-item>
        <el-form-item label="音频文件" prop="audioUrl">
          <AudioUpload v-model="form.audioUrl" />
        </el-form-item>
        <el-form-item label="歌词文件" prop="lyricUrl">
          <LyricUpload v-model="form.lyricUrl" />
        </el-form-item>
        <el-form-item label="时长(秒)" prop="duration">
          <el-input-number
            v-model="form.duration"
            :min="0"
            placeholder="请输入时长"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
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
import { getSongList, createSong, updateSong, deleteSong, getArtistList, getAlbumList } from '@/api'
import type { Song, Artist, Album } from '@/types'
import { ImageUpload, AudioUpload, LyricUpload } from '@/components'

const tableData = ref<Song[]>([])
const artistList = ref<Artist[]>([])
const albumList = ref<Album[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

const searchForm = reactive({
  name: '',
  artistId: undefined as number | undefined
})

const pagination = reactive({
  page: 1,
  size: 20,
  total: 0
})

const form = reactive<Partial<Song>>({
  name: '',
  artistId: undefined,
  albumId: undefined,
  cover: '',
  audioUrl: '',
  lyricUrl: '',
  duration: undefined
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入歌曲名称', trigger: 'blur' }],
  artistId: [{ required: true, message: '请选择歌手', trigger: 'change' }]
}

const loadSongs = async () => {
  try {
    const res = await getSongList({
      page: pagination.page,
      size: pagination.size,
      name: searchForm.name || undefined,
      artistId: searchForm.artistId
    })
    tableData.value = res.records
    pagination.total = res.total
  } catch (error) {
    ElMessage.error('加载歌曲列表失败')
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

const loadAlbums = async (artistId?: number) => {
  try {
    const res = await getAlbumList({
      page: 1,
      size: 1000,
      artistId
    })
    albumList.value = res.records
  } catch (error) {
    ElMessage.error('加载专辑列表失败')
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadSongs()
}

const handleReset = () => {
  searchForm.name = ''
  searchForm.artistId = undefined
  pagination.page = 1
  loadSongs()
}

const handleAdd = () => {
  dialogTitle.value = '添加歌曲'
  dialogVisible.value = true
}

const handleEdit = (row: Song) => {
  dialogTitle.value = '编辑歌曲'
  Object.assign(form, row)
  if (row.artistId) {
    loadAlbums(row.artistId)
  }
  dialogVisible.value = true
}

const handleArtistChange = (artistId: number) => {
  form.albumId = undefined
  loadAlbums(artistId)
}

const handleDelete = async (row: Song) => {
  try {
    await ElMessageBox.confirm('确定要删除该歌曲吗？', '提示', {
      type: 'warning'
    })
    await deleteSong(row.id)
    ElMessage.success('删除成功')
    loadSongs()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    submitLoading.value = true
    try {
      if (form.id) {
        await updateSong(form.id, form)
        ElMessage.success('更新成功')
      } else {
        await createSong(form)
        ElMessage.success('创建成功')
      }
      dialogVisible.value = false
      loadSongs()
    } catch (error) {
      ElMessage.error('操作失败')
    } finally {
      submitLoading.value = false
    }
  })
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
  Object.keys(form).forEach((key) => {
    form[key as keyof typeof form] = undefined
  })
}

const formatDuration = (seconds: number | undefined) => {
  if (!seconds) return '--'
  const minutes = Math.floor(seconds / 60)
  const secs = seconds % 60
  return `${minutes}:${secs.toString().padStart(2, '0')}`
}

onMounted(() => {
  loadSongs()
  loadArtists()
})

const getImageUrl = (url: string) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return `http://localhost:8080/api${url}`
}
</script>

<style scoped>
.songs {
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

/* 修复对话框内下拉框问题 */
:deep(.el-dialog__body) {
  overflow: visible !important;
}

:deep(.el-form) {
  overflow: visible !important;
}
</style>
