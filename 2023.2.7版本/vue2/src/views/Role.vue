<template>
    <div>
        <div style="margin-bottom: 30px">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item>用户管理</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
  
          <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
            <el-input style="width: 200px" placeholder="请输入description" suffix-icon="el-icon-message" class="ml-5" v-model="description"></el-input>
            <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-position" class="ml-5"></el-input>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button class="ml-5" type="warn" @click="reset">重置</el-button>
          </div>
  
          <div style="margin: 10px 0">
            <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
            <el-button type="danger" @click="delBatch">批量删除 <i class="el-icon-remove-outline" ></i></el-button>
          </div>
  
          <el-table :data="tableData" border: stripe :header-cell-class-name="headerBg"
         
          @selection-change="handleSelectionChange" >
            <el-table-column type="selection" width="55" >
            </el-table-column>
            <!-- 这里对应的prop是服务器form表单传过来对应的值，所以 这些值必须是User的属性-->
            <el-table-column prop="id" label="编号"  >
            </el-table-column>
            <el-table-column prop="name" label="姓名" >
            </el-table-column>
            <el-table-column prop="description" label="描述">
            </el-table-column>
            <el-table-column label="操作"  width="280" align="center">
              <template slot-scope="scope">
                <el-button type="info" slot="reference" @click="selectMenu(scope.row.id)">分配菜单 <i class="el-icon-menu"></i></el-button>
                <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
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
      <el-form-item label="唯一标识" >
        <el-input v-model="form.flag" autocomplete="off"></el-input>
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

  <el-dialog title="菜单分配" :visible.sync="menuDialogVis">
    <el-form label-width="100px" size="small">
   
      <el-tree
      :data="menuData"
      :props="props"
      show-checkbox
      ref="tree"
      node-key="id"  
      :default-expanded-keys="[1, 3]"
      :default-checked-keys="checks"
      >
      <!-- 这里要指定传过来的tree数据中的key是id，
      那么运用这个函数this.$refs.tree.getCheckedKeys()时的key才是id -->
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span><i :class="data.icon"></i> {{ data.name }}</span>
     </span>
    </el-tree>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary"  @click="saveRoleMenu">确 定</el-button>
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
                pageNum:1,
                pageSize:10,
                description:"",
                menuDialogVis:false,
                dialogFormVisible:false,
                total:0,
                form:{},
                multipleSelection:[],
                headerBg: 'headerBg',
                menuData:[],
                checks: [],
                roleId:0,
                props:{
                  label:'name'
                  //这个name对应的是 后端传过来的数据中 对应的name，即要渲染到树形结构上作为可选择的那些文字
                  //也可以改成/path，icon等等
                }

          }
        },
        methods: {
            load(){
      this.request.get("/role/page",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          name:this.name,
          description:this.description
        }
      }).then(res =>{
      this.tableData=res.data.records;
      this.total=res.total;
      })

    },
    save(){
      console.log(this.form)
      this.request.post("/role/",this.form).then(res =>{
        if(res){
          this.$message.success("保存成功")
          this.dialogFormVisible = false
        }else{
          this.$message.success("保存失败")
        }
      })
      this.load()
    },
    saveRoleMenu(roleId){
        this.request.post("/role/roleMenu/" + this.roleId,this.$refs.tree.getCheckedKeys()).then(res =>{
            if(res.code === '200'){
              this.$message.success("绑定成功")
              this.menuDialogVis =false
            }else{
              this.$message.error(res.msg)
            }
        })
    },
    handleDelete(id){
      this.request.delete("http://47.113.199.13/role/"+id).then(res =>{
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
        let ids=this.multipleSelection.map(v=>v.id)
        console.log(ids)
        this.request.post("/role/del/batch",ids).then(res =>{
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
    handleAdd(){
      this.dialogFormVisible =true
      this.form={}
    },
    selectMenu(roleId){
        this.menuDialogVis=true
        this.roleId=roleId
        this.request.get("/menu",{
        // params:{
        //   name:" ",
        // }
      }).then(res =>{
        this.menuData =res.data
      })

      this.request.get("/role/roleMenu/"+roleId).then(res =>{
        this.checks =res.data
      })

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