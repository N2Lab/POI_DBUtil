# これは何?
エクセルからSQL,model等を自動生成るするツールです

# 利用方法
ソースコードをチェックアウトし、Eclipseなどでビルドしてください。

# 実行方法

### Mroonga無効化モード (AWSなど)
com.ams.poi.xls2sql.XLS2SQL SampleTableDefs.xls ./sql mysql UTF8 UTF8 utf8mb4 utf8 MROONGA_DISABLE

### Mroonga無効化 & id指定による連番出力モード (InsertAll内の {client_id} を 指定idの連番(例は 1〜33)でInsert_all.sqlを出力する)
com.ams.poi.xls2sql.XLS2SQL SampleTableDefs.xls ./sql mysql UTF8 UTF8 utf8mb4 utf8 MROONGA_DISABLE 11 33

# excelファイル仕様
SampleTableDefs.xls  を参照
