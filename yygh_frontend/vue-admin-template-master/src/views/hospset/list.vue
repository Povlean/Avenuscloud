<template>
  <div>
    <div class="app-container">
      <el-form :inline="true" class="demo-form-inline">
        <el-form-item>
            <el-input  v-model="searchObj.hosname" placeholder="医院名称"/>
        </el-form-item>
        <el-form-item>
            <el-input v-model="searchObj.hoscode" placeholder="医院编号"/>
        </el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="getList()">查询</el-button>
      </el-form>

      <div>
        <el-button type="danger" size="mini" @click="removeRows()">批量删除</el-button>
      </div>


      <!-- banner列表 -->
      <el-table
      :data="list" stripe style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"/>
        <el-table-column type="index" width="50"/>
        <el-table-column prop="hosname" label="医院名称"/>
        <el-table-column prop="hoscode" label="医院编号"/>
        <el-table-column prop="apiUrl" label="api基础路径" width="200"/>
        <el-table-column prop="contactsName" label="联系人姓名"/>
        <el-table-column prop="contactsPhone" label="联系人手机"/>
        <el-table-column label="状态" width="80">
        <template slot-scope="scope">
                  {{ scope.row.status === 1 ? '可用' : '不可用' }}
        </template>
        </el-table-column>
        <el-table-column label="操作" width="280" align="center">
          <template slot-scope="scope">
              <el-button type="danger" size="mini" 
                icon="el-icon-delete" @click="removeDataById(scope.row.id)">删除</el-button>
              <el-button v-if="scope.row.status==1" type="primary" size="mini" 
                icon="el-icon-delete" @click="lockHostSet(scope.row.id,0)">锁定</el-button>
              <el-button v-if="scope.row.status==0" type="danger" size="mini" 
                icon="el-icon-delete" @click="lockHostSet(scope.row.id,1)">取消锁定</el-button>
            <router-link :to="'/hospSet/edit/'+scope.row.id">
              <el-button type="primary" size="mini" icon="el-icon-edit"></el-button>
            </router-link>                  
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
      :current-page="current"
      :page-size="limit"
      :total="50"
      style="padding: 30px 0; text-align: center;"
      layout="total, prev, pager, next, jumper"
      @current-change="getList"/>

    </div>
  </div>
</template>

<script>
import hospset from '@/api/hospset'
export default {
  data() {
    return {
      current:1,
      limit:3,
      searchObj:{},
      list:[],
      multipleSelection: [] 
    }
  },
  methods: {
    // 锁定和取消锁定
    lockHostSet(id, status) {
      hospset.lockHostSet(id, status)
        .then(response => {
          // 刷新
          this.getList()
        })
    },
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },
    // 批量删除
    removeRows() {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          var idList = []
          // 遍历数组得到每个id值，设置到idList里面
          for(var i = 0;i < this.multipleSelection.length;i++) {
            var obj = this.multipleSelection[i]
            var id = obj.id
            idList.push(id)
          }
          // 调用接口
          hospset.batchRemoveHospSet(idList)
            // 提示信息
            .then(response => {
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
              // 刷新页面
              this.getList()
            })
        })
    },
    getList(page=1) {
      this.current = page
      hospset.getHospSetList(this.current,this.limit,this.searchObj)
            .then(response => {
              this.list = response.data.records
              console.log(response)
            })
            .catch(error => {
              console.log(error)
            })
    },
    removeDataById(id) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          hospset.deleteHospSetById(id)
            // 提示信息
            .then(response => {
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
              // 刷新页面
              this.getList()
            })
        })
    }
  },
  created() {
    this.getList()
  }
}
</script>

<style>

</style>