<template>
  <div class="app-container">
      <el-form label-width="120px">
         <el-form-item label="医院名称">
            <el-input v-model="hospitalSet.hosname"/>
         </el-form-item>
         <el-form-item label="医院编号">
            <el-input v-model="hospitalSet.hoscode"/>
         </el-form-item>
         <el-form-item label="api基础路径">
            <el-input v-model="hospitalSet.apiUrl"/>
         </el-form-item>
         <el-form-item label="联系人姓名">
            <el-input v-model="hospitalSet.contactsName"/>
         </el-form-item>
         <el-form-item label="联系人手机">
            <el-input v-model="hospitalSet.contactsPhone"/>
         </el-form-item>
         <el-form-item>
            <el-button type="primary" @click="saveOrUpdate()">保存</el-button>
         </el-form-item>
      </el-form>
   </div>
</template>

<script>
import hospset from '../../api/hospset';
export default {
  data() {
    return {
      hospitalSet:{} 
    }  
  },
  created() {
    //获取路由id值 调用接口得到医院设置信息
    if(this.$route.params && this.$route.params.id) {
      const id = this.$route.params.id
      this.getHostSet(id)
    } else {
      this.hospitalSet = {}
    }
  },
  methods: {
    getHostSet(id) {
      hospset.getHospSet(id)
        .then(response => {
          this.hospitalSet = response.data
        })
    },
    // 保存方法
    save() {
      hospset.saveHospSet(this.hospitalSet)
        .then(response => {
          // 提示
          this.$message({
            type: 'success',
            message: '保存成功'
          })
          // 刷新页面
          this.$router.push({path:'/hospSet/list'})
        })
    },
    // 修改方法
    update() {
      hospset.updateHospSet(this.hospitalSet)
        .then(response => {
          // 提示
          this.$message({
            type: 'success',
            message: '修改成功'
          })
          // 刷新页面
          this.$router.push({path:'/hospSet/list'})
        })
    },
    saveOrUpdate() {
      // 判断添加还是修改
      if (!this.hospitalSet.id) {
        // 保存
        this.save()
      } else {
        // 修改
        this.update()
      }
    }
  }
}
</script>

<style>

</style>