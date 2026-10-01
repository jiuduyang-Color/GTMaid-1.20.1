# libs 目录说明

编译本项目需要把以下 jar 放进**本目录**（因授权原因不随仓库分发）：

| 文件 | 说明 |
|---|---|
| `touhoulittlemaid-1.5.3-forge+mc1.20.1.jar` | 车万女仆 1.5.3 |
| `gtceu-1.20.1-7.5.3.jar` | GregTech CEu Modern 7.5.3 |
| `ldlib-1.0.40.b.jar` | GTCEu 内嵌依赖 |
| `registrate-1.3.11.jar` | 同上 |
| `configuration-2.2.0.jar` | 同上 |
| `mixinextras-0.5.3.jar` | 同上 |

后 4 个可以从 `gtceu-*.jar` 内部解出：

```
gtceu-1.20.1-7.5.3.jar
└── META-INF/jarjar/
    ├── ldlib-forge-1.20.1-1.0.40.b.jar
    ├── Registrate-MC1.20-1.3.11.jar
    ├── configuration-forge-1.20.1-2.2.0.jar
    └── mixinextras-forge-0.5.3.jar
```

解出后重命名为上表中的文件名即可（`build.gradle` 里的 `flatDir` 依赖按 `名称-版本.jar` 解析）。
