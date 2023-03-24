<template>
  <div>
      <div style="margin-bottom: 30px">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>用户管理</el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div style="margin: 10px 0">
          <el-input style="width: 200px" placeholder="请输入姓名" suffix-icon="el-icon-search" v-model="name"></el-input>
          <el-input style="width: 200px" placeholder="请输入路径" suffix-icon="el-icon-message" class="ml-5" v-model="path"></el-input>
          <el-input style="width: 200px" placeholder="请输入描述" suffix-icon="el-icon-position" class="ml-5" v-model="description"></el-input>
          <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
          <el-button class="ml-5" type="warn" @click="reset">重置</el-button>
        </div>

        <div style="margin: 10px 0">
         
          <el-button type="primary" @click="handleAdd(null)">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
          <el-button type="danger" @click="delBatch">批量删除 <i class="el-icon-remove-outline" ></i></el-button>
      
        </div>

        <el-table :data="tableData" border: stripe :header-cell-class-name="headerBg"
        row-key="id" default-expand-all 
        @selection-change="handleSelectionChange" >
          <el-table-column type="selection" width="55" >
          </el-table-column>
          <!-- 这里对应的prop是服务器form表单传过来对应的值，所以 这些值必须是User的属性-->
          <el-table-column prop="id" label="编号"  >
          </el-table-column>
          <el-table-column prop="name" label="姓名" >
          </el-table-column>
          <el-table-column prop="path" label="路径">
          </el-table-column>
          <el-table-column prop="pagePath" label="页面路径">
          </el-table-column>
          
           <el-table-column label="图标" class-name="fontSize18" align="center" label-class-name="fontSize12">
            <template slot-scope="scope"  >
              <span :class="scope.row.icon" style="font-size:22px" />
            </template>
          </el-table-column>
          <el-table-column prop="description" label="描述"> 
          </el-table-column>
          <el-table-column label="操作"  width="300" align="center">
            <template slot-scope="scope">
              <el-button type="primary" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
              <el-button type="success" @click="handleAdd(scope.row.id)" v-if="!scope.row.pid && !scope.row.path">新增子菜单 <i class="el-icon-edit"></i></el-button>
              <el-button type="danger" @click="handleDelete(scope.row.id)">删除 <i class="el-icon-remove-outline"></i></el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="padding: 10px 0">
          <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
              :current-page="pageNum"
              :page-sizes="[2, 5, 10, 20]"
              :page-size="pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total">
          </el-pagination>
        </div>


<el-dialog title="用户信息" :visible.sync="dialogFormVisible">
  <el-form label-width="100px" size="small">
  
    <el-form-item label="名字" >
      <el-input v-model="form.name" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item label="path" >
      <el-input v-model="form.path" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item label="pagePath" >
      <el-input v-model="form.pagePath" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item label="icon" >
      <el-select clearable v-model="form.icon" placeholder="请选择" style="width: 100%">
        <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
          <i :class="item.value" /> {{ item.name }}
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="描述" >
      <el-input v-model="form.description" autocomplete="off"></el-input>
    </el-form-item>
  </el-form>
  <div slot="footer" class="dialog-footer">
    <el-button @click="dialogFormVisible = false">取 消</el-button>
    <el-button type="primary"  @click="save">确 定</el-button>
  </div>
</el-dialog>
  </div>
</template>

<script>
  export default {
      data() {
          return {
              tableData: [],
              name:"",
              path:"",
              description:"",
              pageNum:1,
              pageSize:10,
              description:"",
              dialogFormVisible:false,
              total:0,
              form:{},
              multipleSelection:[],
              headerBg: 'headerBg',
              options:[]
          }
      },
      methods: {
          load(){
    this.request.get("/menu",{
      params:{
        // pageNum:this.pageNum,
        // pageSize:this.pageSize,
        // name:this.name,
        // path:this.path,
        // description:this.description
      }
    }).then(res =>{
    this.tableData=res.data;
  

    })
  },
  save(){
    console.log(this.form)
    this.request.post("/menu/",this.form).then(res =>{
      if(res){
        this.$message.success("保存成功")
        this.dialogFormVisible = false
        this.load();
      }else{
        this.$message.success("保存失败")
      }
    })
    this.load()
  },
  addChildren(pid){
      this.dialogFormVisible =true
      this.form={}
      if(pid){
        this.form.pid=pid
      }
  },
  handleAdd(pid){
    console.log("xxxx");
      //请求图标的数据
      this.request.get("/menu/icons").then(res =>{
      this.options=res.data
    })
    this.dialogFormVisible=true
    this.form={}
      if(pid){
        this.form.pid=pid
      }
  },
  handleDelete(id){
    this.request.delete("http://47.113.199.13:9090/menu/"+id).then(res =>{
      if(res){
        this.$message.success("delete successfully")
        this.load()
      }else{
        this.$message.success("delete failure")
      }
    })
  },
  handleSelectionChange(val){
    console.log(val)
    this.multipleSelection=val
  },
  handleEdit(row){
    this.form=row
    this.dialogFormVisible=true

    //请求图标的数据
    this.request.get("/menu/icons").then(res =>{
      this.options=res.data
    })


  },
  delBatch(){
      let ids=this.multipleSelection.map(v=>v.id)
      console.log(ids)
      this.request.post("/menu/del/batch",ids).then(res =>{
      if(res){
        this.$message.success("批量删除成功")
        this.load()
        
      }else{
        this.$message.success("delete failure")
      }
    })
    
  },
  reset(){
      this.name="",
      this.path="",
      this.description=""
      this.load();
  },
  handleSizeChange(pageSize){
    console.log(pageSize)
    this.pageSize=pageSize;
    this.load();
  },
  handleCurrentChange(pageNum){
    console.log(pageNum)
    this.pageNum=pageNum;
    this.load();
  },
  // handleAdd(){
  //   this.dialogFormVisible =true
  //   this.form={}
  // },

handleExcelImportSuccess() {
  this.$message.success("导入成功")
  this.load()
}
},
created(){
this.load();
}
  }
</script>

<style>
.headerBg {
background: #eee!important;
}
</style>