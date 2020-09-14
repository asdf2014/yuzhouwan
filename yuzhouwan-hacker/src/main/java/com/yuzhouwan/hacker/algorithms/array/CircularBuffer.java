package com.yuzhouwan.hacker.algorithms.array;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function：Circular Buffer
 *
 * @param <T>
 * @author Benedict Jin
 * @since 2017/03/01
 */
public class CircularBuffer<T> {

    /*
     * helper macro to distinguish between real in place fifo where the fifo
     * array is a part of the structure and the fifo type where the array is
     * outside of the fifo structure.
     */
    public void isKfifoPtr() {
    }

    /**
     * declareKfifoPtr - macro to declare a fifo pointer object.
     *
     * @fifo: name of the declared fifo
     * @type: type of the fifo elements
     */
    public void declareKfifoPtr() {
    }

    /**
     * initKfifo - Initialize a fifo declared by DECLARE_KFIFO.
     *
     * @fifo: name of the declared fifo datatype
     */
    public void initKfifo() {
    }

    /**
     * DECLARE_KFIFO - macro to declare a fifo object.
     *
     * @fifo: name of the declared fifo
     * @type: type of the fifo elements
     * @size: the number of elements in the fifo, this must be a power of 2
     */

    /**
     * defineKfifo - macro to define and initialize a fifo.
     *
     * @fifo: name of the declared fifo datatype
     * @type: type of the fifo elements
     * @size: the number of elements in the fifo, this must be a power of 2
     * <p>
     * Note: the macro can be used for global and local fifo data type variables.
     */
    public void defineKfifo() {
    }

    public void kfifoUintMustCheckHelper() {
    }

    public void kfifoIntMustCheckHelper() {
    }

    /**
     * kfifoInitialized - Check if the fifo is initialized.
     *
     * @fifo: address of the fifo to check
     * <p>
     * Return %true if fifo is initialized, otherwise %false.
     * Assumes the fifo was 0 before.
     */
    public void kfifoInitialized() {
    }

    /**
     * kfifoEsize - returns the size of the element managed by the fifo.
     *
     * @fifo: address of the fifo to be used
     */
    public void kfifoEsize() {

    }

    /**
     * kfifo_recsize - returns the size of the record length field.
     *
     * @fifo: address of the fifo to be used
     */
    public void kfifoRecsize() {
    }

    /**
     * kfifo_size - returns the size of the fifo in elements.
     *
     * @fifo: address of the fifo to be used
     */
    public void kfifoSize() {
    }

    /**
     * kfifo_reset - removes the entire fifo content.
     *
     * @fifo: address of the fifo to be used
     * <p>
     * Note: usage of kfifo_reset() is dangerous. It should be only called when the
     * fifo is exclusived locked or when it is secured that no other thread is
     * accessing the fifo.
     */
    public void kfifoReset() {
    }

    /**
     * kfifo_reset_out - skip fifo content.
     *
     * @fifo: address of the fifo to be used
     * <p>
     * Note: The usage of kfifo_reset_out() is safe until it will be only called
     * from the reader thread and there is only one concurrent reader. Otherwise
     * it is dangerous and must be handled in the same way as kfifo_reset().
     */
    public void kfifoResetOut() {
    }

    /**
     * kfifo_len - returns the number of used elements in the fifo.
     *
     * @fifo: address of the fifo to be used
     */
    public void kfifoLen() {
    }

    /**
     * kfifo_is_empty - returns true if the fifo is empty.
     *
     * @fifo: address of the fifo to be used
     */
    public void kfifoIsEmpty() {
    }

    /**
     * kfifo_is_full - returns true if the fifo is full.
     *
     * @fifo: address of the fifo to be used
     */
    public void kfifoIsFull() {

    }

    /**
     * kfifo_avail - returns the number of unused elements in the fifo.
     *
     * @fifo: address of the fifo to be used
     */
    public void kfifoAvail() {

    }

    /**
     * kfifo_skip - skip output data.
     *
     * @fifo: address of the fifo to be used
     */
    public void kfifoSkip() {
    }

    /**
     * kfifo_peek_len - gets the size of the next fifo record.
     *
     * @fifo: address of the fifo to be used
     * <p>
     * This function returns the size of the next fifo record in number of bytes.
     */
    public void kfifoPeekLen() {
    }

    /**
     * kfifo_alloc - dynamically allocates a new fifo buffer.
     *
     * @fifo: pointer to the fifo
     * @size: the number of elements in the fifo, this must be a power of 2
     * @gfp_mask: get_free_pages mask, passed to kmalloc()
     * <p>
     * This macro dynamically allocates a new fifo buffer.
     * <p>
     * The numer of elements will be rounded-up to a power of 2.
     * The fifo will be release with kfifo_free().
     * Return 0 if no error, otherwise an error code.
     */
    public void kfifoAlloc() {
    }

    /**
     * kfifo_free - frees the fifo.
     *
     * @fifo: the fifo to be freed
     */
    public void kfifoFree() {
    }

    /**
     * kfifo_init - initialize a fifo using a preallocated buffer.
     *
     * @fifo: the fifo to assign the buffer
     * @buffer: the preallocated buffer to be used
     * @size: the size of the internal buffer, this have to be a power of 2
     * <p>
     * This macro initialize a fifo using a preallocated buffer.
     * <p>
     * The numer of elements will be rounded-up to a power of 2.
     * Return 0 if no error, otherwise an error code.
     */
    public void kfifoInit() {
    }

    /**
     * kfifo_put - put data into the fifo.
     *
     * @fifo: address of the fifo to be used
     * @val: the data to be added
     * <p>
     * This macro copies the given value into the fifo.
     * It returns 0 if the fifo was full. Otherwise it returns the number
     * processed elements.
     * <p>
     * Note that with only one concurrent reader and one concurrent
     * writer, you don't need extra locking to use these macro.
     */
    public void kfifoPut() {
    }

    /**
     * kfifo_get - get data from the fifo.
     *
     * @fifo: address of the fifo to be used
     * @val: address where to store the data
     * <p>
     * This macro reads the data from the fifo.
     * It returns 0 if the fifo was empty. Otherwise it returns the number
     * processed elements.
     * <p>
     * Note that with only one concurrent reader and one concurrent
     * writer, you don't need extra locking to use these macro.
     */
    public void kfifoGet() {
    }

    /**
     * kfifo_peek - get data from the fifo without removing.
     *
     * @fifo: address of the fifo to be used
     * @val: address where to store the data
     * <p>
     * This reads the data from the fifo without removing it from the fifo.
     * It returns 0 if the fifo was empty. Otherwise it returns the number
     * processed elements.
     * <p>
     * Note that with only one concurrent reader and one concurrent
     * writer, you don't need extra locking to use these macro.
     */
    public void kfifoPeek() {
    }

    /**
     * kfifo_in - put data into the fifo.
     *
     * @fifo: address of the fifo to be used
     * @buf: the data to be added
     * @n: number of elements to be added
     * <p>
     * This macro copies the given buffer into the fifo and returns the
     * number of copied elements.
     * <p>
     * Note that with only one concurrent reader and one concurrent
     * writer, you don't need extra locking to use these macro.
     */
    public void kfifoIn() {
    }

    /**
     * kfifo_in_spinlocked - put data into the fifo using a spinlock for locking.
     *
     * @fifo: address of the fifo to be used
     * @buf: the data to be added
     * @n: number of elements to be added
     * @lock: pointer to the spinlock to use for locking
     * <p>
     * This macro copies the given values buffer into the fifo and returns the
     * number of copied elements.
     */

    public void kfifoInSpinlocked() {

    }

    /**
     * kfifo_out - get data from the fifo.
     *
     * @fifo: address of the fifo to be used
     * @buf: pointer to the storage buffer
     * @n: max. number of elements to get
     * <p>
     * This macro get some data from the fifo and return the numbers of elements
     * copied.
     * <p>
     * Note that with only one concurrent reader and one concurrent
     * writer, you don't need extra locking to use these macro.
     */
    public void kfifoOut() {
    }

    /**
     * kfifo_out_spinlocked - get data from the fifo using a spinlock for locking.
     *
     * @fifo: address of the fifo to be used
     * @buf: pointer to the storage buffer
     * @n: max. number of elements to get
     * @lock: pointer to the spinlock to use for locking
     * <p>
     * This macro get the data from the fifo and return the numbers of elements
     * copied.
     */
    public void kfifoOutSpinlocked() {

    }

    /**
     * kfifo_from_user - puts some data from user space into the fifo.
     *
     * @fifo: address of the fifo to be used
     * @from: pointer to the data to be added
     * @len: the length of the data to be added
     * @copied: pointer to output variable to store the number of copied bytes
     * <p>
     * This macro copies at most @len bytes from the @from into the
     * fifo, depending of the available space and returns -EFAULT/0.
     * <p>
     * Note that with only one concurrent reader and one concurrent
     * writer, you don't need extra locking to use these macro.
     */
    public void kfifoFromUser() {

    }

    /**
     * kfifo_to_user - copies data from the fifo into user space.
     *
     * @fifo: address of the fifo to be used
     * @to: where the data must be copied
     * @len: the size of the destination buffer
     * @copied: pointer to output variable to store the number of copied bytes
     * <p>
     * This macro copies at most @len bytes from the fifo into the
     * @to buffer and returns -EFAULT/0.
     * <p>
     * Note that with only one concurrent reader and one concurrent
     * writer, you don't need extra locking to use these macro.
     */
    public void kfifoToUser() {
    }

    /**
     * kfifo_dma_in_prepare - setup a scatterlist for DMA input.
     *
     * @fifo: address of the fifo to be used
     * @sgl: pointer to the scatterlist array
     * @nents: number of entries in the scatterlist array
     * @len: number of elements to transfer
     * <p>
     * This macro fills a scatterlist for DMA input.
     * It returns the number entries in the scatterlist array.
     * <p>
     * Note that with only one concurrent reader and one concurrent
     * writer, you don't need extra locking to use these macros.
     */
    public void kfifoDmaInPrepare() {
    }

    /**
     * kfifo_dma_in_finish - finish a DMA IN operation.
     *
     * @fifo: address of the fifo to be used
     * @len: number of bytes to received
     * <p>
     * This macro finish a DMA IN operation. The in counter will be updated by
     * the len parameter. No error checking will be done.
     * <p>
     * Note that with only one concurrent reader and one concurrent
     * writer, you don't need extra locking to use these macros.
     */
    public void kfifoDmaInFinish() {

    }

    /**
     * kfifo_dma_out_prepare - setup a scatterlist for DMA output.
     *
     * @fifo: address of the fifo to be used
     * @sgl: pointer to the scatterlist array
     * @nents: number of entries in the scatterlist array
     * @len: number of elements to transfer
     * <p>
     * This macro fills a scatterlist for DMA output which at most @len bytes
     * to transfer.
     * It returns the number entries in the scatterlist array.
     * A zero means there is no space available and the scatterlist is not filled.
     * <p>
     * Note that with only one concurrent reader and one concurrent
     * writer, you don't need extra locking to use these macros.
     */
    public void kfifoDmaOutPrepare() {
    }

    /**
     * kfifo_dma_out_finish - finish a DMA OUT operation.
     *
     * @fifo: address of the fifo to be used
     * @len: number of bytes transferred
     * <p>
     * This macro finish a DMA OUT operation. The out counter will be updated by
     * the len parameter. No error checking will be done.
     * <p>
     * Note that with only one concurrent reader and one concurrent
     * writer, you don't need extra locking to use these macros.
     */
    public void kfifoDmaOutFinish() {
    }

    /**
     * kfifo_out_peek - gets some data from the fifo.
     *
     * @fifo: address of the fifo to be used
     * @buf: pointer to the storage buffer
     * @n: max. number of elements to get
     * <p>
     * This macro get the data from the fifo and return the numbers of elements
     * copied. The data is not removed from the fifo.
     * <p>
     * Note that with only one concurrent reader and one concurrent
     * writer, you don't need extra locking to use these macro.
     */
    public void kfifoOutPeek() {
    }

    /**
     * k-FIFO.
     */
    class Kfifo {
        int in;
        int out;
        int mask;
        int esize;
        T data;
    }
}
