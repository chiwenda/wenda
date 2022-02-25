<template>
  <div class="app-container" style="min-height: calc(100vh - 84px);">
    <el-row :gutter="6">
      <el-col :span="24">
        <el-form style="height: 10%;" :inline="true" :model="queryParams" size="small" v-show="showSearch">
          <el-form-item label="商品名称">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入商品名称"
              clearable
              maxlength="50" show-word-limit/>
          </el-form-item>
          <el-form-item label="商品编号" style="vertical-align: bottom;">
            <el-input
              v-model="queryParams.code"
              placeholder="请输入商品编号"
              clearable
              maxlength="50" show-word-limit
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
              <el-option
                v-for="item in [{value:1,label:'上架'},{value:2,label:'下架'}]"
                :key="item.value"
                :label="item.label"
                :value="Number(item.value)">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="商品类型">
            <el-select v-model="queryParams.status" placeholder="请选择商品类型" clearable size="small">
              <el-option
                v-for="item in [{value:1,label:'实物商品'},{value:2,label:'虚拟商品'}]"
                :key="item.value"
                :label="item.label"
                :value="Number(item.value)">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      showSearch: true,
      queryParams: {}
    }
  }
}
</script>

<style scoped>

</style>
