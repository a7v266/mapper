SELECT writer.id                                    a1808407022,
       writer.birthdate                             a773512559,
       (SELECT STRING_AGG(CAST(book_writer.book_id AS VARCHAR), ',')
        FROM book_writer book_writer
        WHERE book_writer.writer_id = writer.id) AS a1182463628,
       writer.image_id                              a1131963943,
       writer.writer_name                           a493844439
FROM writer writer
WHERE writer.birthdate >= ?
