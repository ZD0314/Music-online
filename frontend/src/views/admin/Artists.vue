<template>
  <div class="artists">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>歌手列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            添加歌手
          </el-button>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="歌手名称">
          <el-input
            v-model="searchForm.name"
            placeholder="请输入歌手名称"
            clearable
          />
        </el-form-item>
        <el-form-item label="流派">
          <el-select
            v-model="searchForm.genre"
            placeholder="请选择流派"
            clearable
            popper-class="custom-select-dropdown"
            style="width: 150px"
          >
            <el-option label="流行" value="流行" />
            <el-option label="摇滚" value="摇滚" />
            <el-option label="民谣" value="民谣" />
            <el-option label="电子" value="电子" />
            <el-option label="古典" value="古典" />
            <el-option label="爵士" value="爵士" />
          </el-select>
        </el-form-item>
        <el-form-item label="语种">
          <el-select
            v-model="searchForm.language"
            placeholder="请选择语种"
            clearable
            popper-class="custom-select-dropdown"
            style="width: 150px"
          >
            <el-option label="华语" value="华语" />
            <el-option label="欧美" value="欧美" />
            <el-option label="日语" value="日语" />
            <el-option label="韩语" value="韩语" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="头像" width="100">
          <template #default="{ row }">
            <el-avatar :src="row.avatar" :size="50">
              {{ row.name.charAt(0) }}
            </el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="歌手名称" />
        <el-table-column prop="nameEn" label="英文名" />
        <el-table-column prop="genre" label="流派" width="100" />
        <el-table-column prop="language" label="语种" width="100" />
        <el-table-column prop="initial" label="首字母" width="100" />
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
        @size-change="loadArtists"
        @current-change="loadArtists"
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
        <el-form-item label="歌手名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入歌手名称" />
        </el-form-item>
        <el-form-item label="英文名" prop="nameEn">
          <el-input v-model="form.nameEn" placeholder="请输入英文名" />
        </el-form-item>
        <el-form-item label="流派" prop="genre">
          <el-select
            v-model="form.genre"
            placeholder="请选择流派"
            style="width: 100%"
            popper-class="custom-select-dropdown"
          >
            <el-option label="流行" value="流行" />
            <el-option label="摇滚" value="摇滚" />
            <el-option label="民谣" value="民谣" />
            <el-option label="电子" value="电子" />
            <el-option label="古典" value="古典" />
            <el-option label="爵士" value="爵士" />
          </el-select>
        </el-form-item>
        <el-form-item label="语种" prop="language">
          <el-select
            v-model="form.language"
            placeholder="请选择语种"
            style="width: 100%"
            popper-class="custom-select-dropdown"
          >
            <el-option label="华语" value="华语" />
            <el-option label="欧美" value="欧美" />
            <el-option label="日语" value="日语" />
            <el-option label="韩语" value="韩语" />
          </el-select>
        </el-form-item>
        <el-form-item label="首字母" prop="initial">
          <el-input v-model="form.initial" placeholder="请输入首字母" maxlength="1" />
        </el-form-item>
        <el-form-item label="头像" prop="avatar">
          <ImageUpload
            v-model="form.avatar"
            :width="100"
            :height="100"
            placeholder="上传头像"
          />
        </el-form-item>
        <el-form-item label="简介" prop="introduction">
          <el-input
            v-model="form.introduction"
            type="textarea"
            :rows="4"
            placeholder="请输入歌手简介"
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
import { getArtistList, createArtist, updateArtist, deleteArtist } from '@/api'
import type { Artist } from '@/types'
import { ImageUpload } from '@/components'

const searchForm = reactive({
  name: '',
  genre: '',
  language: ''
})

const pagination = reactive({
  page: 1,
  size: 20,
  total: 0
})

const tableData = ref<Artist[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref('添加歌手')
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

const form = reactive<Partial<Artist>>({
  name: '',
  nameEn: '',
  genre: '',
  language: '',
  initial: '',
  avatar: '',
  introduction: ''
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入歌手名称', trigger: 'blur' }],
  genre: [{ required: true, message: '请选择流派', trigger: 'change' }],
  language: [{ required: true, message: '请选择语种', trigger: 'change' }],
  initial: [{ required: true, message: '请输入首字母', trigger: 'blur' }]
}

const loadArtists = async () => {
  try {
    const data = await getArtistList({
      page: pagination.page,
      size: pagination.size,
      name: searchForm.name || undefined,
      genre: searchForm.genre || undefined,
      language: searchForm.language || undefined
    })
    tableData.value = data.records
    pagination.total = data.total
  } catch (error) {
    console.error('获取歌手列表失败:', error)
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadArtists()
}

const handleReset = () => {
  searchForm.name = ''
  searchForm.genre = ''
  searchForm.language = ''
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '添加歌手'
  dialogVisible.value = true
}

const handleEdit = (row: Artist) => {
  dialogTitle.value = '编辑歌手'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row: Artist) => {
  ElMessageBox.confirm(`确定要删除歌手"${row.name}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteArtist(row.id)
      ElMessage.success('删除成功')
      loadArtists()
    } catch (error) {
      console.error('删除失败:', error)
    }
  })
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
  Object.keys(form).forEach(key => {
    form[key as keyof typeof form] = ''
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (form.id) {
          await updateArtist(form.id, form)
          ElMessage.success('更新成功')
        } else {
          await createArtist(form)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        loadArtists()
      } catch (error) {
        console.error('操作失败:', error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

onMounted(() => {
  loadArtists()
})
</script>

<style scoped>
.artists {
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
