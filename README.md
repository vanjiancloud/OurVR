# OurVR

OurVR，是OurBIM云引擎的产品模块之一。基于OurBIM稳定、高效、低延时的云端GPU实时渲染能力，云VR（Cloud VR）可将头戴显示器HMD转变为可显示专业级质量图像的高清VR显示器，并可进行实时数据交互。
本仓库是OurVR SDK的官方示例代码。

## 使用步骤

### 1.引入libs文件

![image](https://github.com/vanjiancloud/OurVR/assets/139726482/11f3613a-372d-469f-88ff-4a09574429bf)

### 2.在build.gradle文件下引入
```sh
dependencies {
    // 引入libs目录下所有的aar文件
    fileTree("libs") {
        include("*.aar")
    }.forEach { file ->
        implementation(files(file))
    }
}
```

