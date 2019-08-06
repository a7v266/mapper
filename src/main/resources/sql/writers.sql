select writer0_.id          as id1_2_0_,
       book2_.id            as id1_0_1_,
       writer0_.writer_name as writer_n2_2_0_,
       book2_.book_name     as book_nam2_0_1_,
       books1_.writer_id    as writer_i1_1_0__,
       books1_.book_id      as book_id2_1_0__
from writer writer0_
         left outer join book_writer books1_ on writer0_.id = books1_.writer_id
         left outer join book book2_ on books1_.book_id = book2_.id
