# mini-DBMS

# Description
> Designing and implementing a simple **DataBase Management System**

>> - Custom queries
>> - DBMS manages only one database
>> - No syntax errors handling for query parsing
>> - Support only three data types

# Query Language
> **CREATE** *table_name* *number_of_columns* *data_type_of_each_column*

> **INSERT** *tabe_name* *values_of_each_column*

> **SELECTALL** *table_name*

> **SELECT** *table_name* *column_index* *condition*

> **FILL** *table_name* *csv_file*    

> **CLEAN** : remove all files from **DB** directory

# Data Types
> **int**

> **float**

> **stringT** : **T** String Length

# Architecture
> ![alt text](https://github.com/izemaghilas/mini-DBMS/blob/main/dbms_architecture.jpg?raw=true)

> ## **Files Access**
>> Simplified version of **PageDirectory** to store *PAGES* in table's dedicated file (as each table has it's own file)

>> **HEAPFILES** for *RECORDS* organization 

> ## **Buffer Management**
>> **LRU** as the *replacement policy*  
