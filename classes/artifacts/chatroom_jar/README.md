# mychatServer

#### 介绍
服务端代码，包括验证用户，消息分类处理等操作，没有消息队列机制。
学完android后做的课程设计
数据库连接使用的是jdbc。主要实现的是用户和消息的检查。
部分功能有些不完善，在传递用户头像的时候数据库用的是blob字段。
找了很久没找到socket传输这种数据后将其还原成图片的办法。就放弃了这个功能