#### server-demo功能介绍
封装支付通道使用，目前已经打通【易极付】支付通道


##### server-demo 使用介绍
1.配置相关配置文件

- **易极付** ：
    因为采用了第三方jar包的形式，导致易极付配置文件比较特殊，需要注意一下几点：
    
    a. 【openapi-sdk.properties】 文件为代扣配置文件，【application-${activatedProperties}.properties】为代付配置文件。
    
    b.  如果需要做maven配置文件区分，可以参考demo工程，
    
    c.  由于第三方jar包中对于环境选择的方式比较古老，只能采用【active profiles】 或者使用 【vm options】 进行环境设计

    d.  【keystore】目录下按照环境区分，分别放置了生产和测试环境的加密私钥和公钥，生产环境时请和易极付官方联系。获得对应的prod环境对应配置文件。
    
    e.  



