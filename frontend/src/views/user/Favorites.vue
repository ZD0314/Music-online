<template>
  <div class="favorites-page">
    <h2>我的收藏</h2>

    <!-- 类型筛选 -->
    <el-radio-group v-model="currentType" @change="fetchFavorites" class="type-filter">
      <el-radio-button :label="null">全部</el-radio-button>
      <el-radio-button :label="1">歌曲</el-radio-button>
      <el-radio-button :label="2">专辑</el-radio-button>
      <el-radio-button :label="3">歌单</el-radio-button>
    </el-radio-group>

    <!-- 收藏列表 -->
    <el-table :data="favorites" v-loading="loading" style="width: 100%">
      <el-table-column label="封面" width="100">
        <template #default="{ row }">
          <img :src="getImageUrl(row.targetCover)" class="cover" />
        </template>
      </el-table-column>
      <el-table-column prop="targetName" label="名称" />
      <el-table-column prop="artistName" label="歌手" width="150" />
      <el-table-column label="类型" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.type === 1">歌曲</el-tag>
          <el-tag v-else-if="row.type === 2" type="success">专辑</el-tag>
          <el-tag v-else-if="row.type === 3" type="warning">歌单</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="收藏时间" width="180">
        <template #default="{ row }">
          {{ formatTime(row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button type="text" @click="goToDetail(row)">查看</el-button>
          <el-button type="text" @click="handleRemove(row)">取消收藏</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 空状态 -->
    <el-empty v-if="!loading && favorites.length === 0" description="暂无收藏" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getFavoriteList, removeFavorite } from '@/api'
import { getImageUrl, formatTime } from '@/utils/format'
import type { Favorite } from '@/types'

const router = useRouter()

const loading = ref(false)
const favorites = ref<Favorite[]>([])
const currentType = ref<number | null>(null)

// 获取收藏列表
const fetchFavorites = async () => {
  loading.value = true
  try {
    favorites.value = await getFavoriteList(currentType.value || undefined)
  } catch (error) {
    ElMessage.error('获取收藏列表失败')
  } finally {
    loading.value = false
  }
}

// 取消收藏
const handleRemove = async (favorite: Favorite) => {
  try {
    await ElMessageBox.confirm('确定要取消收藏吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await removeFavorite(favorite.id)
    ElMessage.success('已取消收藏')
    fetchFavorites()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('取消收藏失败')
    }
  }
}

// 跳转到详情页
const goToDetail = (favorite: Favorite) => {
  switch (favorite.type) {
    case 1: // 歌曲
      ElMessage.info('歌曲详情页待实现')
      break
    case 2: // 专辑
      router.push(`/albums/${favorite.targetId}`)
      break
    case 3: // 歌单
      router.push(`/playlists/${favorite.targetId}`)
      break
  }
}

// 格式化时间
// const formatTime = (time: string) => {
//   return new Date(time).toLocaleString('zh-CN')
// }

onMounted(() => {
  fetchFavorites()
})
</script>

<style scoped>
.favorites-page {
  padding: 20px;
}

.favorites-page h2 {
  margin-bottom: 20px;
}

.type-filter {
  margin-bottom: 20px;
}

.cover {
  width: 60px;
  height: 60px;
  border-radius: 4px;
  object-fit: cover;
}
</style>
