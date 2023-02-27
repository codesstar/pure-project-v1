<template>
    <div>
        <div style="margin-bottom: 30px">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item>用户管理</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
  
          <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="fname"></el-input>
            <el-input style="width: 200px" placeholder="请输入remark" suffix-icon="el-icon-message" class="ml-5" v-model="remark"></el-input>
            <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-position" class="ml-5"></el-input>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button class="ml-5" type="warn" @click="reset">重置</el-button>
          </div>
  
          <div style="margin: 10px 0">
            <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
            <el-button type="danger" @click="delBatch">批量删除 <i class="el-icon-remove-outline" ></i></el-button>
            <el-upload action="http://localhost:9090/user/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
              <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
            </el-upload>
            <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>
          </div>
  
          <el-table :data="tableData" border: stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange" >
            <el-table-column type="selection" width="55" >
            </el-table-column>
            <!-- 这里对应的prop是服务器form表单传过来对应的值，所以 这些值必须是User的属性-->
            <el-table-column prop="fid" label="编号" width="140" >
            </el-table-column>
            <el-table-column prop="fname" label="姓名" width="120">
            </el-table-column>
            <el-table-column prop="fcount" label="数量" width="140">
            </el-table-column>
            <el-table-column prop="price" label="价格">
            </el-table-column>
            <el-table-column prop="remark" label="评价">
            </el-table-column>
            <el-table-column label="操作"  width="200" align="center">
              <template slot-scope="scope">
                <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
                <el-button type="danger" @click="handleDelete(scope.row.fid)">删除 <i class="el-icon-remove-outline"></i></el-button>
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
  
  
  <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
    <el-form label-width="100px" size="small">
      <el-form-item label="用户名" >
        <el-input v-model="form.fname" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="数量" >
        <el-input v-model="form.fcount" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="价格" >
        <el-input v-model="form.price" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="评价" >
        <el-input v-model="form.remark" autocomplete="off"></el-input>
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
                fname:"",
                pageNum:1,
                pageSize:10,
                remark:"",
                dialogFormVisible:false,
                total:0,
                form:{},
                multipleSelection:[],
                headerBg: 'headerBg'
            }
        },
        methods: {
            load(){
      this.request.get("/user/page",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          fname:this.fname,
          remark:this.remark
        }
      }).then(res =>{
         console.log(res);
        console.log("xxx")
      this.tableData=res.records;
      this.total=res.total;

      })
    },
    save(){
      console.log(this.form)
      this.request.post("/user/",this.form).then(res =>{
        if(res){
          this.$message.success("保存成功")
          this.dialogFormVisible = false
        }else{
          this.$message.success("保存失败")
        }
      })
      this.load()
    },
    handleDelete(id){
      this.request.delete("http://localhost:9090/user/"+id).then(res =>{
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
    },
    delBatch(){
        let ids=this.multipleSelection.map(v=>v.fid)
        console.log(ids)
        this.request.post("/user/del/batch",ids).then(res =>{
        if(res){
          this.$message.success("批量删除成功")
          this.load()
          
        }else{
          this.$message.success("delete failure")
        }
      })
      
    },
    reset(){
        this.fname="",
        this.remark=""
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
    handleAdd(){
      this.dialogFormVisible =true
      this.form={}
    },
    exp() {
	window.open("http://localhost:9090/user/export")
},
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